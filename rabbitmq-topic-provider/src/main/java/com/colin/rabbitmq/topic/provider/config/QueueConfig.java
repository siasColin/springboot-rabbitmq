package com.colin.rabbitmq.topic.provider.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 创建消息队列
 * @author Administrator
 *
 */

@Configuration
public class QueueConfig {

	/**
	 * 创建队列
	 * @return
	 */
	@Bean
	public Queue createQueue(){
		return new Queue("hello-queue");
	}
}
