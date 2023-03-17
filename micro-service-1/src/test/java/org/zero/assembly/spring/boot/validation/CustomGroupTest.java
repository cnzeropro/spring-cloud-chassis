package org.zero.assembly.spring.boot.validation;

import org.junit.jupiter.api.Test;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2023/2/10
 */
class CustomGroupTest {

    @Test
    void test() {
        Class<?> createClass = CustomGroup.Crud.Create.class;
        System.out.println(createClass);
        Class<?> readClass = CustomGroup.Crud.Read.class;
        System.out.println(readClass);
    }
}