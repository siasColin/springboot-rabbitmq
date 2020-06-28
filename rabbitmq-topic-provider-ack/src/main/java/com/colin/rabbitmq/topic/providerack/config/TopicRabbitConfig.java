package com.colin.rabbitmq.topic.providerack.config;

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
 * @Package: com.colin.rabbitmq.topic.providerack.config
 * @Author: sxf
 * @Date: 2020-6-2
 * @Description: 主题模式 交换器
 */

@Configuration
public class TopicRabbitConfig {

    @Value("${mq.config.routingkey.dlx}")
    private String dlxRoutingKey;

    @Value("${mq.config.dlxexchange}")
    private String dlxExchange;

    @Value("${mq.config.queue.name.dlx}")
    private String dlxQueue;

    /**
     * 声明死信交换器
     *      死信队列跟交换器类型没有关系 不一定为DirectExchange,不影响该类型交换器的特性
     *      durable：是否持久化
     *      autoDelete：所有消费者断开后是否自动删除
     * @return
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchange,true,false);
    }
    /**
     * 声明死信队列
     *
     * @return
     */
    @Bean
    public Queue dlxQueue() {
        return new Queue(dlxQueue);
    }

    /**
     * 私信队列绑定交换器
     * @return
     */
    @Bean
    Binding bindingDlxExchangeAndLogDlxQueue() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with(dlxRoutingKey);
    }

    /**
     * 交换器名称
     */
    @Value("${mq.config.exchange}")
    private String exchangeName;

    /**
     * 全类型日志队列名称
     */
    @Value("${mq.config.queue.name.all}")
    private String logAllQueueName;

    /**
     * info类型日志队列名称
     */
    @Value("${mq.config.queue.name.info}")
    private String logInfoQueueName;

    /**
     * error类型日志队列名称
     */
    @Value("${mq.config.queue.name.error}")
    private String logErrorQueueName;


    /**
     * 全类型日志路由键
     */
    @Value("${mq.config.routingkey.all}")
    private String logAllRoutingkey;

    /**
     * info类型日志路由键
     */
    @Value("${mq.config.routingkey.info}")
    private String logInfoRoutingkey;

    /**
     * error类型日志路由键
     */
    @Value("${mq.config.routingkey.error}")
    private String logErrorRoutingkey;

    /**
     * 创建交换器
     *     durable：是否持久化
     *     autoDelete：所有消费者断开后是否自动删除
     * @return
     */
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(exchangeName,true,false);
    }

    /**
     * 创建全类型日志队列
     * @return
     */
    @Bean
    public Queue logAllQueue() {
        /**
         * 队列有五个参数（String name, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments）
         * name：队列名称
         * durable：持久性
         * exclusive：排他性（独立性）
         * autoDelete：自动删除
         * arguments：其他相关参数
         *        x-message-ttl(Time-To-Live)：消息存活时间，单位毫秒
         * 　　　　x-expires：队列没有访问超时时，自动删除（包含没有消费的消息），单位毫秒。
         * 　　　　x-max-length：限制队列最大长度（新增后挤出最早的），单位个数。
         * 　　　　x-max-length-bytes ：限制队列最大容量
         * 　　　　x-dead-letter-exchange：死信交换机，将删除/过期的数据，放入指定交换机。
         * 　　　　x-dead-letter-routing-key：死信路由，将删除/过期的数据，放入指定routingKey
         * 　　　　x-max-priority：队列优先级。
         * 　　　　x-queue-mode：对列模式，默认lazy（将数据放入磁盘，消费时放入内存）。
         * 　　　　x-queue-master-locator：镜像队列
         */
        Map<String, Object> args = new HashMap<String, Object>(3);
        //声明死信交换器
        args.put("x-dead-letter-exchange", dlxExchange);
        //声明死信路由键
        args.put("x-dead-letter-routing-key", dlxRoutingKey);
        //声明队列消息过期时间，,配合死信队列可以实现延迟队列功能
        args.put("x-message-ttl", 10000);
        return new Queue(logAllQueueName,true, false, false, args);
    }

    /**
     * 创建info类型日志队列
     * @return
     */
    @Bean
    public Queue logInfoQueue() {
        return new Queue(logInfoQueueName);
    }

    /**
     * 创建error类型日志队列
     * @return
     */
    @Bean
    public Queue logErrorQueue() {
        return new Queue(logErrorQueueName);
    }


    /**
     * 将全类型队列和交换器（topicExchange）绑定，绑定路由键为 ：logAllRoutingkey
     * 这样只要是消息携带的路由键是 logAllRoutingkey,才会分发到该队列
     * @return
     */
    @Bean
    Binding bindingExchangeAndLogAllQueue() {
        return BindingBuilder.bind(logAllQueue()).to(topicExchange()).with(logAllRoutingkey);
    }

    /**
     * 将info类型队列和交换器（topicExchange）绑定，绑定路由键为 ：logInfoRoutingkey
     * 这样只要是消息携带的路由键是 logInfoRoutingkey,才会分发到该队列
     * @return
     */
    @Bean
    Binding bindingExchangeAndLogInfoQueue() {
        return BindingBuilder.bind(logInfoQueue()).to(topicExchange()).with(logInfoRoutingkey);
    }

    /**
     * 将error类型队列和交换器（topicExchange）绑定，绑定路由键为 ：logErrorRoutingkey
     * 这样只要是消息携带的路由键是 logErrorRoutingkey,才会分发到该队列
     * @return
     */
    @Bean
    Binding bindingExchangeAndLogErrorQueue() {
        return BindingBuilder.bind(logErrorQueue()).to(topicExchange()).with(logErrorRoutingkey);
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
