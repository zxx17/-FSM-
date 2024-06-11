CREATE TABLE applications (
                              id INT AUTO_INCREMENT PRIMARY KEY COMMENT '应用ID',
                              name VARCHAR(100) NOT NULL UNIQUE COMMENT '应用名称',
                              description TEXT COMMENT '应用描述',
                              start_state VARCHAR(50) NOT NULL COMMENT '开始状态编码',
                              end_state VARCHAR(50) NOT NULL COMMENT '结束状态编码',
                              created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB;

CREATE TABLE states (
                        id INT AUTO_INCREMENT PRIMARY KEY COMMENT '状态ID',
                        application_id INT NOT NULL COMMENT '应用ID',
                        code VARCHAR(50) NOT NULL COMMENT '状态编码',
                        name VARCHAR(100) NOT NULL COMMENT '状态名称',
                        created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        INDEX idx_application_id(application_id),
                        INDEX idx_code(code)
) ENGINE=InnoDB;

CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
                       application_id INT NOT NULL COMMENT '应用ID',
                       role_name VARCHAR(50) NOT NULL COMMENT '角色名称',
                       created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                       updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                       INDEX idx_application_id(application_id),
                       INDEX idx_role_name(role_name)
) ENGINE=InnoDB;


CREATE TABLE role_auths (
                            id INT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
                            role_id INT NOT NULL COMMENT '角色ID',
                            from_state VARCHAR(50) NOT NULL COMMENT '起始状态编码',
                            to_state VARCHAR(50) NOT NULL COMMENT '目标状态编码',
                            created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            INDEX idx_role_id(role_id),
                            INDEX idx_from_state(from_state),
                            INDEX idx_to_state(to_state)
) ENGINE=InnoDB;

CREATE TABLE workflows (
                           id INT AUTO_INCREMENT PRIMARY KEY COMMENT '流程ID',
                           application_id INT NOT NULL COMMENT '应用ID',
                           name VARCHAR(100) NOT NULL COMMENT '流程名称',
                           description TEXT COMMENT '流程描述',
                           created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           INDEX idx_application_id(application_id),
                           INDEX idx_name(name)
) ENGINE=InnoDB;


CREATE TABLE workflow_states (
                                 id INT AUTO_INCREMENT PRIMARY KEY COMMENT '流程状态ID',
                                 workflow_id INT NOT NULL COMMENT '流程ID',
                                 state_code VARCHAR(50) NOT NULL COMMENT '状态编码',
                                 created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 INDEX idx_workflow_id(workflow_id),
                                 INDEX idx_state_code(state_code)
) ENGINE=InnoDB;


CREATE TABLE workflow_events (
                                 id INT AUTO_INCREMENT PRIMARY KEY COMMENT '事件ID',
                                 workflow_id INT NOT NULL COMMENT '流程ID',
                                 event_name VARCHAR(50) NOT NULL COMMENT '事件名称',
                                 from_state VARCHAR(50) NOT NULL COMMENT '起始状态编码',
                                 to_state VARCHAR(50) NOT NULL COMMENT '目标状态编码',
                                 role VARCHAR(50) NOT NULL COMMENT '执行角色',
                                 created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 INDEX idx_workflow_id(workflow_id),
                                 INDEX idx_event_name(event_name),
                                 INDEX idx_from_state(from_state),
                                 INDEX idx_to_state(to_state),
                                 INDEX idx_role(role)
) ENGINE=InnoDB;


CREATE TABLE workflow_instances (
                                    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '实例ID',
                                    workflow_id INT NOT NULL COMMENT '流程ID',
                                    current_state VARCHAR(50) NOT NULL COMMENT '当前状态编码',
                                    history TEXT COMMENT '历史状态记录',
                                    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    INDEX idx_workflow_id(workflow_id),
                                    INDEX idx_current_state(current_state)
) ENGINE=InnoDB;