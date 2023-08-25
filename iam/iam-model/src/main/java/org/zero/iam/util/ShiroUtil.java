package org.zero.iam.util;

import lombok.experimental.UtilityClass;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.zero.iam.model.dto.LoginUser;

import java.util.Optional;

/**
 * @author zero
 * @since 2021/8/22
 */
@UtilityClass
public class ShiroUtil {

    public String getUsername() {
        return Optional.ofNullable(SecurityUtils.getSubject())
                .map(Subject::getPrincipal)
                .map(LoginUser.class::cast)
                .map(LoginUser::getUsername)
                .orElse(null);
    }
}
