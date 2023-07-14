package org.zero.component.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author cnzeropro@qq.com
 * @date 2021/6/1
 */
@Slf4j
public class CustomMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.info("start insert fill ...");
        }

        strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        // strictInsertFill(metaObject, "createTime", Date::new, Date.class);
        strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        // strictInsertFill(metaObject, "updateTime", Date::new, Date.class);

        String userName = getUserName();
        strictInsertFill(metaObject, "createBy", String.class, userName);
        strictInsertFill(metaObject, "updateBy", String.class, userName);

        strictInsertFill(metaObject, "deleted", Boolean.class, Boolean.FALSE);
        strictInsertFill(metaObject, "version", Integer.class, 1);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (log.isDebugEnabled()) {
            log.info("start update fill ...");
        }

        strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        // strictUpdateFill(metaObject, "updateTime", Date::new, Date.class);
        strictUpdateFill(metaObject, "updateBy", this::getUserName, String.class);
    }

    /**
     * 获取 spring security 当前的用户名
     *
     * @return 当前用户名
     */
    private String getUserName() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getName)
                .orElse(null);
    }

    /**
     * 获取 apache shiro 当前的用户名
     *
     * @return 当前用户名
     */
    // private String getUserName() {
    //     return Optional.ofNullable(SecurityUtils.getSubject())
    //             .map(Subject::getPrincipal)
    //             .map(LoginUser.class::cast)
    //             .map(LoginUser::getUserName)
    //             .orElse(null);
    // }
}