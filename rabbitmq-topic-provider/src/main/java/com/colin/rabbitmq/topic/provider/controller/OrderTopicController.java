package com.colin.rabbitmq.topic.provider.controller;

import com.colin.rabbitmq.topic.provider.entities.ResultCode;
import com.colin.rabbitmq.topic.provider.entities.ResultInfo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Package: com.colin.rabbitmq.topic.provider.controller
 * @Author: sxf
 * @Date: 2020-5-31
 * @Description: 模拟订单日志处理器 Topic
 */
@Controller
@RequestMapping("/orderTopic")
public class OrderTopicController {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    //exchange 交换器名称
    @Value("${mq.config.exchange}")
    private String exchange;

    @RequestMapping("/send")
    @ResponseBody
    public ResultInfo send(String debugMsg,String infoMsg,String warnMsg,String errorMsg){
        ResultInfo resultInfo = ResultInfo.of(ResultCode.SUCCESS);
        //向消息队列发送消息
        //参数一：交换器名称。
        //参数二：路由键
        //参数三：消息
        this.rabbitTemplate.convertAndSend(this.exchange,"order.log.debug", "order.log.debug....."+debugMsg);
        this.rabbitTemplate.convertAndSend(this.exchange,"order.log.info", "order.log.info....."+infoMsg);
        this.rabbitTemplate.convertAndSend(this.exchange,"order.log.warn", "order.log.warn....."+warnMsg);
        this.rabbitTemplate.convertAndSend(this.exchange,"order.log.error", "order.log.error....."+errorMsg);
        return resultInfo;
    }
}
