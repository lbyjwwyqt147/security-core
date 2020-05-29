package pers.liujunyi.cloud.security.security.hander;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.util.BaseConstant;
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
        Map<String, Object> map =  new HashMap<>();
        // 如果请求是 /oauth/authorize 进行 authorization_code 授权，则跳转到登录页
        if (BaseConstant.OAUTH_AUTHORIZE.equals(httpServletRequest.getRequestURI())) {
            // authorization_code 授权时 未登录时跳转到登录页面
            LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint =  new LoginUrlAuthenticationEntryPoint("/login");
            loginUrlAuthenticationEntryPoint.commence(httpServletRequest, httpServletResponse, e);
        }  else {
            // 非 /oauth/authorize  请求， 则返回json 给前端
            if (!HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
                log.info("http请求url：" + httpServletRequest.getRequestURI() + " 身份认证失败.");
                log.info(e.getMessage());
                e.printStackTrace();
                map.put("success", false);
                map.put("description", e.getMessage());
                map.put("path", httpServletRequest.getServletPath());
                map.put("timestamp", DateTimeUtils.getCurrentDateTimeAsString());
                if (e instanceof InsufficientAuthenticationException) {
                    map.put("status", ErrorCodeEnum.AUTHORITY.getCode());
                    map.put("message", ErrorCodeEnum.TOKEN_INVALID.getMessage());
                } else {
                    map.put("status", ErrorCodeEnum.LOGIN_WITHOUT.getCode());
                    map.put("message", ErrorCodeEnum.LOGIN_WITHOUT.getMessage());
                }
                httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            } else {
                httpServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
            ResultUtil.writeJavaScript(httpServletResponse, map);
        }
    }
}
