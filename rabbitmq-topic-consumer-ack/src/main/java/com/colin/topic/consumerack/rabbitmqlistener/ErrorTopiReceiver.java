package com.colin.topic.consumerack.rabbitmqlistener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Package: com.colin.topic.consumerack.rabbitmqlistener
 * @Author: sxf
 * @Date: 2020-6-2
 * @Description:
 */
@Component
@RabbitListener(queues = "${mq.config.queue.name.error}")
public class ErrorTopiReceiver {
    @RabbitHandler
    public void process(@Payload String body, Channel channel, @Headers Map<String, Object> headers){
        //deliveryTag:该消息的index
        long deliveryTag = -1;
        try{
            deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);

            System.out.println("ErrorTopiReceiver消费者收到消息  : " + body.toString());

            channel.basicAck(deliveryTag, false); // false只确认当前一个消息收到，true确认所有consumer获得的消息
        }catch (Exception e){
            e.printStackTrace();
            try {
                /**
                 * 为true会重新放回队列
                 * 使用拒绝后重新入列这个确认模式要谨慎，因为一般都是出现异常的时候，catch异常再拒绝入列，选择是否重入列。
                 * 但是如果使用不当会导致一些每次都被你重入列的消息一直消费-入列-消费-入列这样循环，会导致消息积压。
                 * 这里设置为false，根据业务需要，可以在这里将改条消息存入专门的错误消息队列，或者根据业务需要进行处理
                 */
                channel.basicReject(deliveryTag, false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
