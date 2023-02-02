package com.heifan.code.helper;

import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.management.ManagementFactory;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author z201.coding@gmail.com
 * @date 2020-08-26
 **/
@Component
@Slf4j
public class SpringContentTools {

    @Autowired
    Environment environment;

    @Value("${system.name:unknown}")
    private String systemName;

    @Autowired
    ApplicationContext applicationContext;

    public String instanceInfo() {
        String port = environment.getProperty("local.server.port");
        LinkedHashSet<String> address = NetUtil.localIpv4s();
        String ip = Strings.EMPTY;
        if (!address.isEmpty()) {
            ip = address.iterator().next();
        }
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        return " ip:  " + ip + ":" + port + " pid " + pid;
    }

    public void mapping() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        //获取url与类和方法的对应信息
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
        for (RequestMappingInfo info : map.keySet()) {
            //获取url的Set集合，一个方法可能对应多个url
            Set<String> patterns = info.getPatternsCondition().getPatterns();
            for (String url : patterns) {
                log.info("API URL : {}", url);
            }
        }
    }

    public String getSystemName() {
        return systemName;
    }

}
