package org.zero.demo.mybatisplus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.zero.iam.common.util.SpringSecurityUtil;

import java.time.LocalDateTime;

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

        // 获取 spring security 当前的用户名
        String username = SpringSecurityUtil.getUsername();
        // 获取 apache shiro 当前的用户名
        // String username = ShiroUtil.getUsername();
        strictInsertFill(metaObject, "createBy", String.class, username);
        strictInsertFill(metaObject, "updateBy", String.class, username);

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
        strictUpdateFill(metaObject, "updateBy", SpringSecurityUtil::getUsername, String.class);
    }
}