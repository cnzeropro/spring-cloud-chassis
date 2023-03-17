package org.zero.assembly.spring.boot.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zero (cnzeropro@qq.com)
 * @date 2022/5/31
 */
@Configuration(proxyBeanMethods = false)
public class RabbitMqDeadConfig {
    private static final int MSG_TTL = 60;
    private static final int MAX_LENGTH = 10;

    private static final String NORMAL_EXCHANGE = "normalExchange";

    private static final String FIXED_QUEUE = "fixedQueue";
    private static final String FIXED_ROUTING_KEY = "fixedRoutingKey";

    private static final String TTL_QUEUE = "ttlQueue";
    private static final String TTL_ROUTING_KEY = "ttlRoutingKey";

    private static final String DEAD_EXCHANGE = "deadExchange";
    private static final String DEAD_QUEUE = "deadQueue";
    private static final String DEAD_ROUTING_KEY = "deadRoutingKey";
    /* *******************************************正常配置**************************************************** */

    /**
     * 正常交换机
     */
    @Bean
    public DirectExchange normalExchange() {
        return ExchangeBuilder.directExchange(NORMAL_EXCHANGE).durable(true).build();
    }

    /**
     * 正常队列
     */
    @Bean
    public Queue fixedQueue() {
        // durable: 是否持久化，默认是true
        // exclusive: 是否当前连接使用，默认false。true表示只能被当前创建的连接使用，而且当连接关闭后队列即被删除。其优先级高于durable
        // autoDelete: 是否自动删除，默认false。当没有生产者或者消费者使用此队列，该队列会自动删除。
        return QueueBuilder.durable(FIXED_QUEUE)
                .maxLength(MAX_LENGTH)
                .deadLetterExchange(DEAD_EXCHANGE).deadLetterRoutingKey(DEAD_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定正常队列和正常交换机
     */
    @Bean
    public Binding fixedRouteBinding(Queue fixedQueue, DirectExchange normalExchange) {
        return BindingBuilder.bind(fixedQueue).to(normalExchange).with(FIXED_ROUTING_KEY);
    }

    /**
     * ttl（time to live）队列
     */
    @Bean
    public Queue ttlQueue() {
        return QueueBuilder.durable(TTL_QUEUE)
                .ttl(MSG_TTL * 1000)
                .deadLetterExchange(DEAD_EXCHANGE).deadLetterRoutingKey(DEAD_ROUTING_KEY)
                .build();
    }

    /**
     * 绑定ttl队列和正常交换机
     */
    @Bean
    public Binding ttlRouteBinding(Queue ttlQueue, DirectExchange normalExchange) {
        return BindingBuilder.bind(ttlQueue).to(normalExchange).with(TTL_ROUTING_KEY);
    }

    /* *******************************************死信配置**************************************************** */

    /**
     * 死信交换机
     */
    @Bean
    public DirectExchange deadExchange() {
        return ExchangeBuilder.directExchange(DEAD_EXCHANGE).durable(true).build();
    }

    /**
     * 死信队列
     */
    @Bean
    public Queue deadQueue() {
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }

    /**
     * 绑定死信队列和死信交换机
     */
    @Bean
    public Binding deadRouteBinding(Queue deadQueue, DirectExchange deadExchange) {
        return BindingBuilder.bind(deadQueue).to(deadExchange).with(DEAD_ROUTING_KEY);
    }
}
