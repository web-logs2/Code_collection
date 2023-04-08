package com.heifan.code.mqc.component;

import com.heifan.code.config.rabbit.RabbitmqConfig;
import com.heifan.code.utils.JsonTool;
import com.rabbitmq.client.Channel;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author HiF
 * @description 监听队列
 * @date 2023/2/28 15:08
 */
@Component
@Slf4j
public class ReceiveHandler {
    //监听email队列
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void receive_email(Object msg, Message message, Channel channel) {
        log.info("EMAIL MSG【{}】", msg);
    }

    //监听sms队列
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_SMS})
    public void receive_sms(Object msg, Message message, Channel channel) {
        log.info("SMS MSG【{}】", msg);
    }

}
