package org.zero.common.core.config.spring.netflix.ribbon;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 官网：<a href="https://github.com/Netflix/ribbon">Ribbon</a>
 * 自动装配：{@link org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration}
 *
 * @author Zero (cnzeropro@qq.com)
 * @since 2021/9/8 16:41
 */
@Configuration(proxyBeanMethods = false)
public class RibbonConfig {

    /**
     * 定义全局负载均衡策略：
     * <p>
     * RoundRobinRule：轮询（默认策略）
     * RandomRule：随机
     * RetryRule：先按照RoundRobinRule的策略获取服务，如果获取服务失败则在指定时间内会进行重试
     * WeightedResponseTimeRule：对RoundRobinRule的扩展，响应速度越快的实例选择权重越大，越容易被选择
     * BestAvailableRule：会先过滤掉由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
     * AvailabilityFilteringRule：先过滤掉故障实例，再选择并发较小的实例
     * ZoneAvoidanceRule：复合判断服务所在区域的性能和服务的可用性选择服务器（默认规则）
     */
    @Bean
    public IRule ribbonRule() {
        return new RoundRobinRule();
    }
}
