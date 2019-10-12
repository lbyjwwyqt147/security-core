package pers.liujunyi.cloud.security.security.hander;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.common.redis.RedisTemplateUtils;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.util.DateTimeUtils;
import pers.liujunyi.cloud.security.security.config.MyUserDetailService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/***
 * 文件名称: CustomLoginSuccessHandler.java
 * 文件描述: 登陆成功　后的处理类
 * 公 司:
 * 内容摘要:
 * 其他说明:　当登陆成功　进入此类  系统中我使用LoginController中的登录接口,所以登录成功后不会进入该方法,如果使用默认登录方法登录成功后则会进入该方法
 * 完成日期:2019年10月09日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${server.port}")
    private Integer curPort;
    @Autowired
    private RedisTemplateUtils redisUtil;
    @Autowired
    private MyUserDetailService myUserDetailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Map<String, Object> map = new HashMap<>();
        map.put("status", ErrorCodeEnum.SUCCESS.getCode());
        //获得授权后可得到用户信息
        User userDetails = (User) authentication.getPrincipal();
        //将身份 存储到SecurityContext里
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        //修改最后一次登录时间

        // String token = tokenStore.getAccessToken(authentication);'
        //tokenStore.getAccessToken(authentication);
        log.info(securityContext.getAuthentication().getPrincipal().toString());
        StringBuffer msg = new StringBuffer("用户：");
        msg.append(userDetails.getUsername()).append(" 成功登录系统.");
        log.info(msg.toString());
        map.put("success", true);
        map.put("path", httpServletRequest.getServletPath());
        map.put("message", "登录成功.");
        map.put("userDetails", userDetails);
        map.put("timestamp", DateTimeUtils.getCurrentDateTimeAsString());
        ResultUtil.writeJavaScript(httpServletResponse, map);
    }

    /**
     * 将登录的用户信息存放到redis中
     *
     * @param token
     * @param userDetails
     */
    private void saveUserToRedis(String token, User userDetails) {

    }


}
