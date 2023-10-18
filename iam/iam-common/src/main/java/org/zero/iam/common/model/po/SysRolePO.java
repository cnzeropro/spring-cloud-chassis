package org.zero.iam.common.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.zero.common.data.model.BasePO;

/**
 * @author zero
 * @date 2019/2/10
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysRolePO extends BasePO {
    private String name;
}
