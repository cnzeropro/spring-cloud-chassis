package org.zero.assembly.txlcn;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.context.annotation.Configuration;

/**
 * tx-lcn使用：
 * 服务端和客户端都ok后，在多个客户端需要进行分布式事务管理的方法上使用@LcnTransaction注解即可
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/12/1 14:41
 */
@Configuration(proxyBeanMethods = false)
// 服务端启用注解
//@EnableTransactionManagerServer
// 客户端使用注解，启用分布式事务
@EnableDistributedTransaction
public class TxLcnConfig {
}
