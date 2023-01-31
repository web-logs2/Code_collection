package com.heifan.code.mdc;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author z201.coding@gamil.com
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 8)
@ConditionalOnClass(WebMvcConfigurer.class)
@Slf4j
public class MdcTraceContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String appTraceId = request.getHeader(MdcApiConstant.HTTP_HEADER_TRACE_ID);
        //直接重置mdc，重新设置
        MDC.clear();
        if (StrUtil.isEmpty(appTraceId)) {
            appTraceId = MdcApiContextHandler.currentTraceId();
            MDC.put(MdcApiConstant.HTTP_HEADER_TRACE_ID, appTraceId);
            request.setAttribute(MdcApiConstant.HTTP_HEADER_TRACE_ID, appTraceId);
        }else{
            MDC.put(MdcApiConstant.HTTP_HEADER_TRACE_ID, appTraceId);
        }
        filterChain.doFilter(request, response);
    }
}

