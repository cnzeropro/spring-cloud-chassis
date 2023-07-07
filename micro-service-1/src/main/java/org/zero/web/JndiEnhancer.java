package org.zero.web;

import lombok.SneakyThrows;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2021/8/10 9:15
 */
public class JndiEnhancer {
    /**
     * name like: "java:comp/env/jdbc/test"
     */
    private final String name;
    private DataSource dataSource;

    public JndiEnhancer(String name) {
        this.name = name;
        createDataSource();
    }

    @SneakyThrows
    public void createDataSource() {
        Context context = null;
        try {
            context = new InitialContext();
            dataSource = (DataSource) context.lookup(name);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        } finally {
            if (context != null) {
                context.close();
            }
        }
    }

    public DataSource getDataSource() {
        if (dataSource == null) {
            createDataSource();
        }

        return dataSource;
    }

    @SneakyThrows
    public Connection getConnection() {
        return getDataSource().getConnection();
    }
}
