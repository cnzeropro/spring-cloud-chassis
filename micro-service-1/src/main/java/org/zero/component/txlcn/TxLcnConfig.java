package org.zero.component.txlcn;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.context.annotation.Configuration;

/**
 * tx-lcn使用：
 * 服务端和客户端都ok后，在多个客户端需要进行分布式事务管理的方法上使用@LcnTransaction注解即可
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/12/1 14:41
 * @deprecated tx-lcn已经很久没有更新了，建议使用Alibaba Seata：{@link org.zero.component.spring.alibaba.seata.SeataConfig}
 */
@Deprecated
@Configuration(proxyBeanMethods = false)
// 注意区分两端之后再使用
// 服务端使用注解，启用分布式事务管理器
@EnableTransactionManagerServer
// 客户端使用注解，启用分布式事务
@EnableDistributedTransaction
public class TxLcnConfig {
}
