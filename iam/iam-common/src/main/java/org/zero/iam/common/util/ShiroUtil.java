package org.zero.iam.common.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.zero.iam.model.dto.shiro.ShiroLoginUser;

import java.util.Optional;

/**
 * @author zero
 * @since 2021/8/22
 */
@Slf4j
@UtilityClass
public class ShiroUtil {

    public Optional<ShiroLoginUser> getUserOptWithEx() {
        return Optional.ofNullable(SecurityUtils.getSubject())
                .map(Subject::getPrincipal)
                .map(ShiroLoginUser.class::cast);
    }

    public Optional<ShiroLoginUser> getUserOpt() {
        try {
            return getUserOptWithEx();
        } catch (Exception e) {
            log.warn("Failed to get user info", e);
            return Optional.empty();
        }
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
