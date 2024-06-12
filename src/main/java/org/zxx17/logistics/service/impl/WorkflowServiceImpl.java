package org.zxx17.logistics.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.zxx17.logistics.common.enums.LogisticsStatusEnum;
import org.zxx17.logistics.common.enums.ResultEnum;
import org.zxx17.logistics.common.result.Result;
import org.zxx17.logistics.controller.request.CreateWorkflowRequest;
import org.zxx17.logistics.controller.response.CommonResponse;
import org.zxx17.logistics.controller.response.ErrorResponse;
import org.zxx17.logistics.domain.dto.WorkflowEventsDto;
import org.zxx17.logistics.manager.WorkflowManager;
import org.zxx17.logistics.service.WorkflowService;

/**
 * .
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @since 2024/6/12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowServiceImpl implements WorkflowService {

  private final WorkflowManager workflowManager;

  @Override
  public Result<?> createWorkflow(CreateWorkflowRequest request) {
    List<WorkflowEventsDto> events = request.getEvents();
    // TODO出现fromState-->toState不合理时直接返回
    for (WorkflowEventsDto event : events) {
      LogisticsStatusEnum fromState = LogisticsStatusEnum.valueOf(event.getFromState());
      LogisticsStatusEnum toState = LogisticsStatusEnum.valueOf(event.getToState());

      if (!isValidTransition(fromState, toState)) {
        ErrorResponse response = new ErrorResponse();
        response.setReason(ResultEnum.ILLEGAL_STATE_TRANSITION.getMessage());
        return Result.response(response,
            "Invalid state transition: " + fromState + " --> " + toState,
            ResultEnum.ILLEGAL_STATE_TRANSITION
        );
      }
    }

    return workflowManager.createWorkflow(request);
  }

  private boolean isValidTransition(LogisticsStatusEnum fromState, LogisticsStatusEnum toState) {
    // Ensures transitions adhere to the defined logistics workflow.
    switch (fromState) {
      case PENDING:       // Initial state, can only transition to collected once ready.
        return toState == LogisticsStatusEnum.COLLECTED;

      // After collection, can proceed to payment or directly complete if paid on collection.
      case COLLECTED:
        return toState == LogisticsStatusEnum.PAYING || toState == LogisticsStatusEnum.COMPLETED;

      case PAYING:        // Once payment process is initiated, it should end with being paid.
        return toState == LogisticsStatusEnum.PAID || toState == LogisticsStatusEnum.CANCELLED;

      case PAID:          // Paid items move into transit.
        return toState == LogisticsStatusEnum.TRANSITING;

      case TRANSITING:    // Items in transit are then delivered.
        return toState == LogisticsStatusEnum.DELIVERY;

      case DELIVERY:      // Delivery results in either successful delivery or refusal.
        return toState == LogisticsStatusEnum.DELIVERED
            || toState == LogisticsStatusEnum.REFUSED
            || toState == LogisticsStatusEnum.EXCEPTION;

      case DELIVERED:     // Successfully delivered items are considered completed.
      case REFUSED:       // Refused items also move to completion for record purposes.
      case EXCEPTION:    // Any exceptional cases are resolved by marking as completed.
      case CANCELLED:     // Cancelled orders are closed off as completed.
        return toState == LogisticsStatusEnum.COMPLETED;

      case COMPLETED:     // Completed is a terminal state; no further transitions allowed.
        // 'COMPLETED' should not transition to any other state
        return false;

      default:           // Handles any unexpected states.
        // Log or handle unrecognized status here if needed.
        return false;
    }
  }


}
