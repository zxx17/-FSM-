package org.zxx17.logistics.common.result;

import lombok.Data;
import lombok.Getter;
import org.zxx17.logistics.common.enums.ResultEnum;

/**
 * 统一结果返回.
 *
 * @author Xinxuan Zhuo
 * @version 1.0.0
 * @date 2024/6/11
 */
@Data
public class Result<T> {

  private State state;
  private T data;

  @Getter
  private static class State {

    private final int code;
    private final String msg;

    public State(int code, String message) {
      this.code = code;
      this.msg = message;
    }

  }

  public static <T> Result<T> response(String message, ResultEnum resultEnum) {
    Result<T> result = new Result<>();
    result.state = new State(resultEnum.getCode(), message);
    result.setData(null);
    return result;
  }

  public static <T> Result<T> response(T data, ResultEnum resultEnum, String message) {
    Result<T> result = new Result<>();
    result.state = new State(resultEnum.getCode(), message);
    result.setData(data);
    return result;
  }


}
