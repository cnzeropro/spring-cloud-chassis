DROP TABLE IF EXISTS `sys_log`;

CREATE TABLE `sys_log`
(
  id             BIGINT      NOT NULL COMMENT '主键ID',
  message        CHARACTER LARGE OBJECT NULL DEFAULT NULL COMMENT '日志信息',
  type           TINYINT     NULL DEFAULT NULL COMMENT '日志类型',
  app            VARCHAR(30) NULL DEFAULT NULL COMMENT '日志所属项目',
  module         VARCHAR(30) NULL DEFAULT NULL COMMENT '日志所属模块',
  operation_type TINYINT     NULL DEFAULT NULL COMMENT '操作类型',
  operator       VARCHAR(30) NULL DEFAULT NULL COMMENT '操作者',
  is_successful  BOOLEAN     NULL DEFAULT NULL COMMENT '日志对应操作是否成功',
  operation_time TIMESTAMP   NULL DEFAULT NULL COMMENT '操作时间',
  time_consuming BIGINT      NULL DEFAULT NULL COMMENT '操作耗时',
  create_by      VARCHAR(30) NULL DEFAULT NULL COMMENT '创建人',
  create_time    TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_by      VARCHAR(30) NULL DEFAULT NULL COMMENT '更新人',
  update_time    TIMESTAMP   NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  is_deleted     BOOLEAN     NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (id)
);
