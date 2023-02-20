package com.heifan.code.mdc;

import cn.hutool.core.util.StrUtil;
import com.heifan.code.mdc.dto.HttpApiDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.MDC;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author z201.coding@gmail.com
 **/
@Slf4j
public class MdcApiContextHandler implements MdcApiConstant {

    public static HttpApiDto current() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        String appTraceId = request.getHeader(HTTP_HEADER_TRACE_ID);
        String authorization = request.getHeader(HTTP_TOKEN_HEADER);
        String tenant = request.getHeader(APP_TENANT);
        /**
         * 没有设置就设置下,设置了就直接返回。注意这里必须提前在拦截器中设置好，不然会失效。
         */
        if (StrUtil.isEmpty(appTraceId)) {
            if (log.isDebugEnabled()) {
                log.debug("Created traceId");
            }
            appTraceId = MDC.get(HTTP_HEADER_TRACE_ID);
            if (Strings.isEmpty(appTraceId)) {
                appTraceId = currentTraceId();
            }
            request.setAttribute(HTTP_HEADER_TRACE_ID, appTraceId);
        }
        if (log.isDebugEnabled()) {
            log.debug("Set mdc");
        }
        MDC.put(HTTP_HEADER_TRACE_ID, appTraceId);
        if (StrUtil.isEmpty(authorization)) {
            authorization = Strings.EMPTY;
        }
        if (StrUtil.isEmpty(tenant)) {
            tenant = Strings.EMPTY;
        }
        return HttpApiDto.builder().appTraceId(appTraceId).authorization(authorization).tenant(tenant).build();
    }

    /**
     * 生成日志随机数
     *
     * @return
     */
    public static synchronized String currentTraceId() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        UUID uuid = new UUID(random.nextInt(), random.nextInt());
        StringBuilder st = new StringBuilder(uuid.toString().replace("-", "").toLowerCase());
        st.append(Instant.now().toEpochMilli());
        int i = 0;
        while (i < 3) {
            i++;
            st.append(ThreadLocalRandom.current().nextInt(2));
        }
        return st.toString();
    }


}
