package com.colin.rabbitmq.direct.providerack.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

/**
 * @Package: com.colin.rabbitmq.direct.providerack.config
 * @Author: sxf
 * @Date: 2020-6-2
 * @Description: 直连型交换机 配置
 */

@Configuration
public class DirectRabbitConfig {

    /**
     * 交换器名称
     */
    @Value("${mq.config.exchange.log}")
    private String logExchange;

    /**
     * 队列名称
     */
    @Value("${mq.config.queue.log}")
    private String logQueue;

    /**
     * 路由键名称
     */
    @Value("${mq.config.routingkey.log}")
    private String logRoutingKey;

    /**
     * 声明一个交换器
     * durable：持久性
     * autoDelete：自动删除
     * @return
     */
    @Bean
    DirectExchange logDirectExchange() {
        return new DirectExchange(logExchange,true,false);
    }

    /**
     * 声明队列
     * @return
     */
    @Bean
    public Queue logDirectQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(logQueue,true);
    }

    //绑定  将队列和交换机绑定, 并设置用于匹配键：logRoutingKey
    @Bean
    Binding bindingLogDirect() {
        return BindingBuilder.bind(logDirectQueue()).to(logDirectExchange()).with(logRoutingKey);
    }

    /**
     * Only one ConfirmCallback is supported by each RabbitTemplate:
     *      每个RabbitTemplate对象只支持一个回调
     *      设置为原型的(也就是@Scope=“prototype”)
     * @param connectionFactory
     * @return
     */
    @Bean
    @Scope("prototype")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        return template;
    }
}
