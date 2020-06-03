package com.colin.rabbitmq.topic.providerack.controller;

import com.colin.rabbitmq.topic.providerack.entities.ResultCode;
import com.colin.rabbitmq.topic.providerack.entities.ResultInfo;
import com.colin.rabbitmq.topic.providerack.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Package: com.colin.rabbitmq.topic.providerack.controller
 * @Author: sxf
 * @Date: 2020-6-2
 * @Description: 模拟用户日志处理器 Topic
 * http://127.0.0.1:8003/rabbitmq-topic-provider-ack/order/send
 */

@RestController
public class OrderMessageController implements  RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{
    private static Logger log = LoggerFactory.getLogger(OrderMessageController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 交换器名称
     */
    @Value("${mq.config.exchange}")
    private String exchangeName;

    @GetMapping("/order/send")
    public ResultInfo send() {
        ResultInfo resultInfo = ResultInfo.of(ResultCode.SUCCESS);
        //确认消息已发送到交换机(Exchange)
        rabbitTemplate.setConfirmCallback(this);
        //如果设置备份队列则不起作用
        rabbitTemplate.setMandatory(true);
        //确认消息已发送到队列(Queue)
        rabbitTemplate.setReturnCallback(this);
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String, Object> map = new HashMap<>();
        map.put("messageId", String.valueOf(UUID.randomUUID()));
        map.put("messageData", "message: DEBUGLOG....MSG ");
        map.put("createTime", createTime);
        rabbitTemplate.convertAndSend(exchangeName, "order.log.debug", "order.log.debug:"+JsonUtils.toString(map),new CorrelationData(UUID.randomUUID().toString()));

        map.put("messageData", "message: INFOLOG....MSG ");
        rabbitTemplate.convertAndSend(exchangeName, "order.log.info", "order.log.info:"+JsonUtils.toString(map),new CorrelationData(UUID.randomUUID().toString()));

        map.put("messageData", "message: WARNLOG....MSG ");
        rabbitTemplate.convertAndSend(exchangeName, "order.log.warn", "order.log.warn:"+JsonUtils.toString(map),new CorrelationData(UUID.randomUUID().toString()));

        map.put("messageData", "message: ERRORLOG....MSG ");
        rabbitTemplate.convertAndSend(exchangeName, "order.log.error", "order.log.error:"+JsonUtils.toString(map),new CorrelationData(UUID.randomUUID().toString()));
        return resultInfo;
    }

    /**
     * 回调确认
     * @param correlationData 相关数据
     * @param ack 确认情况
     * @param cause 原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(ack){
            log.info("order_消息发送成功:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
        }else{
            log.info("order_消息发送失败:correlationData({}),ack({}),cause({})",correlationData,ack,cause);
        }
    }

    /**
     * 消息发送到转换器的时候没有对列,配置了备份对列该回调则不生效
     * @param message 消息
     * @param replyCode 回应码
     * @param replyText 应信息
     * @param exchange 交换机
     * @param routingKey 路由键
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("order_消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}",exchange,routingKey,replyCode,replyText,message);
    }
}
