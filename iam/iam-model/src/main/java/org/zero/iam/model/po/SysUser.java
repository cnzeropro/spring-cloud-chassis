package org.zero.iam.model.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zero
 * @date 2019/2/10
 */
@Data
public class SysUser implements Serializable {
    private Long id;
    private String name;
    private String password;
    private Boolean locked;
}
