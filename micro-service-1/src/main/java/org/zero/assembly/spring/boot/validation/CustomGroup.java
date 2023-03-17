package org.zero.assembly.spring.boot.validation;

import javax.validation.groups.Default;

/**
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/3/16
 */
public interface CustomGroup extends Default {
    interface Crud extends CustomGroup {
        interface Create extends Crud {

        }

        interface Update extends Crud {

        }

        interface Read extends Crud {

        }

        interface Delete extends Crud {

        }
    }
}
