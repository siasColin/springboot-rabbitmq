package com.colin.rabbitmq.topic.consumer.rabbitListener;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.stereotype.Component;

/**
 * 全类型日志消息消费者
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
public class AllTopicReceiver {
	private static Logger logger = LoggerFactory.getLogger(AllTopicReceiver.class);
	@RabbitListener(
			bindings=@QueueBinding(
					value=@Queue(value="${mq.config.queue.name.all}"),
					exchange=@Exchange(value="${mq.config.exchange}",type= ExchangeTypes.TOPIC),
					key="${mq.config.routingkey.all}"
			)
	)
	public void process(Message messageorigin, Channel channel){
		String msg = null;
		try {
			/**
			 * 使用Jackson2JsonMessageConverter后，反序列化时要求发送的类和接受的类完全一样（字段，类名，包路径）
 			 */
			Jackson2JsonMessageConverter jackson2JsonMessageConverter =new Jackson2JsonMessageConverter();
			msg = (String)jackson2JsonMessageConverter.fromMessage(messageorigin);
			logger.info("AllTopicReceiver:"+msg.toString());
		}catch (Exception e){
			System.out.println("get order fromBytes message exception"+e.getMessage());
		}
	}
}
