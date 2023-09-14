package org.zero.iam.common.util;

import lombok.experimental.UtilityClass;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.zero.iam.model.dto.shiro.ShiroLoginUser;

import java.util.Optional;

/**
 * @author zero
 * @since 2021/8/22
 */
@UtilityClass
public class ShiroUtil {

    public Optional<ShiroLoginUser> getUserOpt() {
        return Optional.ofNullable(SecurityUtils.getSubject())
                .map(Subject::getPrincipal)
                .map(ShiroLoginUser.class::cast);
    }

    public ShiroLoginUser getUser() {
        return getUserOpt().orElse(null);
    }

    public Optional<String> getUsernameOpt() {
        return getUserOpt().map(ShiroLoginUser::getUsername);
    }

    public String getUsername() {
        return getUsernameOpt().orElse(null);
    }
}
