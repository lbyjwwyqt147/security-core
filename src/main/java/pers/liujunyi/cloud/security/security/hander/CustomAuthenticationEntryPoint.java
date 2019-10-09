package pers.liujunyi.cloud.security.security.hander;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
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
 * 文件名称: CustomAuthenticationEntryPoint.java
 * 文件描述: 身份认证失败　后的处理类
 * 公 司:
 * 内容摘要:
 * 其他说明:　当身份认证失败　进入此类
 * 完成日期:2019年10月09日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
@Component
public class CustomAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info(" ********************* 身份认证 ***************** ");
        log.info("请求url：" + httpServletRequest.getRequestURI() + " 身份认证失败.");
        log.info(e.getMessage());
        log.info(e.getLocalizedMessage());
        e.printStackTrace();
        Map<String, Object> map =  new HashMap<>();
        map.put("status", ErrorCodeEnum.LOGIN_WITHOUT.getCode());
        map.put("message", e.getMessage());
        map.put("description", ErrorCodeEnum.LOGIN_WITHOUT.getMessage());
        map.put("path", httpServletRequest.getServletPath());
        map.put("timestamp", DateTimeUtils.getCurrentDateTimeAsString());
        httpServletResponse.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
        ResultUtil.writeJavaScript(httpServletResponse, map);
    }
}
