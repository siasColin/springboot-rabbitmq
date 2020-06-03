package com.colin.rabbitmq.topic.consumer.rabbitListener;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * error类型日志消息消费者
 * @author colin
 * @RabbitListener bindings:绑定队列
 * @QueueBinding  value:绑定队列的名称
 *                exchange:配置交换器
 *
 * @Queue value:配置队列名称
 *        autoDelete:是否是一个可删除的临时队列
 *
 * @Exchange value:为交换器起个名称
 *           type:指定具体的交换器类型
 */
@Component
public class ErrorTopicReceiver {
	private static Logger logger = LoggerFactory.getLogger(ErrorTopicReceiver.class);

	@RabbitListener(
			bindings=@QueueBinding(
					value=@Queue(value="${mq.config.queue.name.error}"),
					exchange=@Exchange(value="${mq.config.exchange}",type= ExchangeTypes.TOPIC),
					key="${mq.config.routingkey.error}"
			)
	)
	public void process(Message message, Channel channel){
		Map msg = null;
		try {
			/**
			 * 使用Jackson2JsonMessageConverter后，反序列化时要求发送的类和接受的类完全一样（字段，类名，包路径）
			 */
			Jackson2JsonMessageConverter jackson2JsonMessageConverter =new Jackson2JsonMessageConverter();
			/**
			 * 为了测试重试机制
			 * 		发送端我们规定传的是String，这里转Map会报错
			 * 		根据配置文件中的配置，会重试5次
			 */
			msg = (Map)jackson2JsonMessageConverter.fromMessage(message);
			logger.info("ErrorTopicReceiver:"+msg.toString());
		}catch (Exception e){
			System.out.println("get order fromBytes message exception"+e.getMessage());
			throw  e;
		}
	}
}
