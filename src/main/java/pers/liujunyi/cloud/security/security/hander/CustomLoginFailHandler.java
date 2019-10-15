package pers.liujunyi.cloud.security.security.hander;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
 * 文件名称: CustomLoginFailHandler.java
 * 文件描述: 登陆失败后　处理类
 * 公 司:
 * 内容摘要:
 * 其他说明:　登陆失败后　会进入到此类
 * 完成日期:2019年10月09日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
@Component
public class CustomLoginFailHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info(" >>>> 登录失败.........");
        log.info(e.getLocalizedMessage());
        Map<String, Object> map =  new HashMap<>();
        map.put("success", false);
        map.put("status", ErrorCodeEnum.LOGIN_INCORRECT.getCode());
        map.put("path", httpServletRequest.getServletPath());
        map.put("message", e.getLocalizedMessage());
        map.put("timestamp", DateTimeUtils.getCurrentDateTimeAsString());
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        ResultUtil.writeJavaScript(httpServletResponse, map);

    }
}
