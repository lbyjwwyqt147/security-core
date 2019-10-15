package pers.liujunyi.cloud.security.security.hander;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.stereotype.Component;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.util.DateTimeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/***
 * 文件名称: CustomAccessDenieHandler.java
 * 文件描述: 权限认证失败后　处理类
 * 公 司:
 * 内容摘要:
 * 其他说明:　权限认证失败后会进入到此类
 * 完成日期:2019年10月09日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Component
@Log4j2
public class CustomAccessDenieHandler extends OAuth2AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException authException) throws IOException, ServletException {
        log.info("http请求url：" + httpServletRequest.getRequestURI() + " 权限认证失败.");
        log.info(authException.getMessage());
        authException.printStackTrace();
        Map<String, Object> map =  new HashMap<>();
        map.put("success", false);
        map.put("status", ErrorCodeEnum.AUTHORITY.getCode());
        map.put("message", authException.getMessage());
        map.put("description", ErrorCodeEnum.AUTHORITY.getMessage());
        map.put("path", httpServletRequest.getServletPath());
        map.put("timestamp", DateTimeUtils.getCurrentDateTimeAsString());
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        ResultUtil.writeJavaScript(httpServletResponse, map);
    }
}
