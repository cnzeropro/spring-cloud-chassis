package org.zero.iam.common.model.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.zero.common.data.model.BasePO;

/**
 * @author zero
 * @date 2019/2/10
 */
@Data
@SuperBuilder(toBuilder = true)
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SysUserPO extends BasePO {
    private String name;
    private String password;
    private Boolean locked;
}
