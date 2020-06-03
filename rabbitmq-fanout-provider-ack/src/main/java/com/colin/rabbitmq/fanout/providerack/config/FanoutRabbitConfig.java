package com.colin.rabbitmq.fanout.providerack.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Package: com.colin.rabbitmq.direct.providerack.config
 * @Author: sxf
 * @Date: 2020-6-2
 * @Description:
 *      扇型交换器，这个交换机没有路由键概念，就算绑了路由键也是无视的。
 *      交换器在接收到消息后，会直接转发到绑定到它上面的所有队列。
 */

@Configuration
public class FanoutRabbitConfig {

    /**
     * 交换器名称
     */
    @Value("${mq.config.exchange.fanout}")
    private String fanoutExchange;

    /**
     * A队列名称
     */
    @Value("${mq.config.queue.fanoutA}")
    private String fanoutAQueue;


    /**
     * B队列名称
     */
    @Value("${mq.config.queue.fanoutB}")
    private String fanoutBQueue;

    /**
     * 声明一个交换器
     * durable：持久性
     * autoDelete：自动删除
     * @return
     */
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(fanoutExchange,true,false);
    }

    /**
     * 声明A队列
     * @return
     */
    @Bean
    public Queue fanoutAQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(fanoutAQueue,true);
    }

    /**
     * 声明B队列
     * @return
     */
    @Bean
    public Queue fanoutBQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(fanoutBQueue,true);
    }

    //绑定  将A队列和交换机绑定
    @Bean
    Binding bindingFanoutA() {
        return BindingBuilder.bind(fanoutAQueue()).to(fanoutExchange());
    }

    //绑定  将B队列和交换机绑定
    @Bean
    Binding bindingFanoutB() {
        return BindingBuilder.bind(fanoutBQueue()).to(fanoutExchange());
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
