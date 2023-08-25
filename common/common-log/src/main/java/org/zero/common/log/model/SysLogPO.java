/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zero.common.log.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.zero.common.data.model.BasePO;

import javax.validation.constraints.NotBlank;

/**
 * @author zero
 * @since 2021/7/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysLogPO extends BasePO {
    private static final long serialVersionUID = 1L;

    /**
     * 日志类型
     */
    @NotBlank(message = "日志类型不能为空")
    private String type;

    /**
     * 日志标题
     */
    @NotBlank(message = "日志标题不能为空")
    private String title;

    /**
     * 操作IP地址
     */
    private String remoteAddr;

    /**
     * 用户浏览器
     */
    private String userAgent;

    /**
     * 请求URI
     */
    private String requestUri;

    /**
     * 操作方式
     */
    private String method;

    /**
     * 操作提交的数据
     */
    private String params;

    /**
     * 执行时间
     */
    private Long time;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 服务ID
     */
    private String serviceId;

}
