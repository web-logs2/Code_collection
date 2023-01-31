package com.heifan.code.mdc;

/**
 * @author z201.coding@gmail.com
 **/
public interface MdcApiConstant {

    String X_REAL_IP = "x-real-ip";
    /**
     * 请求头跟踪id名。
     */
    String HTTP_HEADER_TRACE_ID = "AppTraceId";

    /**
     * 请求头
     */
    String HTTP_TOKEN_HEADER = "Authorization";

    /**
     * app租户
     */
    String APP_TENANT = "Tenant";

    String CLIENT_BUSINESS_GROUP_SOURCE = "Client-Business-Group-Source";

    String CLIENT_BUSINESS_SOURCE = "Client-Business-Source";

    String CLIENT_BUSINESS_ACTIVITY_SOURCE = "Client-Business-Activity-Source";

    String CLIENT_EVN_SOURCE = "Client-Env-Source";

    String CLIENT_PLATFORM_SOURCE = "Client-Platform-Source";

    String CLIENT_START_TIME = "Client-Start-Time";

    String CLIENT_VERSION_SOURCE = "Client-Version-Source";

}
