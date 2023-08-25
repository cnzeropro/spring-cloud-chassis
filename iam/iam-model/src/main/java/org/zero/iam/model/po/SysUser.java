package org.zero.iam.model.po;

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
public class SysUser extends BasePO {
    private String name;
    private String password;
    private Boolean locked;
}
