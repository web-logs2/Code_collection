package com.heifan.code.mqp.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.heifan.code.config.rabbit.RabbitmqConfig;
import com.heifan.code.domain.result.Result;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/msg")
public class SendMsgController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostMapping("/send")
    public Object sendMsg(HttpServletRequest request, HttpServletResponse response) {
        String uuid = IdUtil.simpleUUID();
        String msg = "=====>生产者发送消息<=====" + "   " + "消息id为" + uuid + "   " + DateUtil.date();
        rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM, "inform.email", msg);
        return Result.success();
    }
}
