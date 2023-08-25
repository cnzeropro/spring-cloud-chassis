package org.zero.common.core.config.txlcn;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://github.com/codingapi/tx-lcn">TX-Lcn</a>
 * 自动装配：
 * {@link com.codingapi.txlcn.tc.TCAutoConfiguration}
 * {@link com.codingapi.txlcn.tm.TMAutoConfiguration}
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2019/1/1 14:41
 * @deprecated tx-lcn已经很久没有更新了，建议使用Alibaba Seata：{@link org.zero.common.core.config.spring.alibaba.seata.SeataConfig}
 */
@Deprecated
// 注意区分两端之后再使用
// 服务端使用注解，启用分布式事务管理器
@EnableTransactionManagerServer
// 客户端使用注解，启用分布式事务
@EnableDistributedTransaction
@Configuration(proxyBeanMethods = false)
public class TxLcnConfig {
}
