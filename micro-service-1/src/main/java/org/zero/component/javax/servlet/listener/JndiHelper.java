package org.zero.component.javax.servlet.listener;

import lombok.SneakyThrows;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Objects;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/8/10 9:15
 */
public class JndiHelper {
    /**
     * name like: "java:comp/env/jdbc/test"
     */
    private final String name;
    private DataSource dataSource;

    public JndiHelper(String name) {
        this.name = name;
        // 懒加载DataSource
        // createDataSource();
    }

    @SneakyThrows
    public void createDataSource() {
        Context context = null;
        try {
            context = new InitialContext();
            dataSource = (DataSource) context.lookup(name);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        } finally {
            if (Objects.nonNull(context)) {
                context.close();
            }
        }
    }

    public DataSource getDataSource() {
        if (Objects.isNull(dataSource)) {
            createDataSource();
        }

        return dataSource;
    }

    @SneakyThrows
    public Connection getConnection() {
        return getDataSource().getConnection();
    }
}
