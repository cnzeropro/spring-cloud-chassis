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

package org.zero.common.log.event;

import org.springframework.context.ApplicationEvent;
import org.zero.common.data.model.po.SysLogPO;

/**
 * 系统日志事件
 *
 * @author zero
   * @date 2022/1/5
 */
public class SysLogEvent extends ApplicationEvent {

    public SysLogEvent(SysLogPO source) {
        super(source);
    }

    @Override
    public SysLogPO getSource() {
        return (SysLogPO) source;
    }
}
