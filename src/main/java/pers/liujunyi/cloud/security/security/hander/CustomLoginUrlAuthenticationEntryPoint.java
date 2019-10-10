package pers.liujunyi.cloud.security.security.hander;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
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
 * 文件名称: CustomLoginUrlAuthenticationEntryPoint.java
 * 文件描述: 未登陆时 的处理类
 * 公 司:
 * 内容摘要:
 * 其他说明:　 因为是前后端分离，所以不能跳转到登陆页面，而是返回未登陆的JSON串
 *           写LoginUrlAuthenticationEntryPoint方法，将该方法里的commence改成返回json串
 * 完成日期:2019年10月09日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
@Component
public class CustomLoginUrlAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.info("请求url：" + httpServletRequest.getRequestURI());
        log.info("  >>>>>>>>>  登录认证 >>>>>>>>>>>>>>> ");
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

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxFlag = request.getHeader("X-Requested-With");
        return ajaxFlag != null && "XMLHttpRequest".equals(ajaxFlag);
    }
}
