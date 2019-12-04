package pers.liujunyi.cloud.security.security.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import pers.liujunyi.cloud.common.exception.DescribeException;
import pers.liujunyi.cloud.common.redis.RedisTemplateUtils;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.util.HttpClientUtils;
import pers.liujunyi.cloud.common.util.TokenLocalContext;
import pers.liujunyi.cloud.common.vo.BaseRedisKeys;
import pers.liujunyi.cloud.security.domain.user.UserDetailsDto;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/***
 * 文件名称: PermitAuthenticationFilter.java
 * 文件描述: 自定义过滤器验证token 返回自定义数据格式
 * 公 司:
 * 内容摘要:
 * 其他说明:　
 * 完成日期:2018年08月27日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
@Component
public class PermitAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_AUTHENTICATION = "bearer ";
    private static final String HEADER_AUTHORIZATION = "Authorization";
    private boolean stateless = true;
    private TokenExtractor tokenExtractor = new BearerTokenExtractor();
    /**
     * @Lazy 解决循环依赖 Requested bean is currently in creation: Is there an unresolvable circular reference
     * @Lazy 让spring 懒惰的初始化这个bean，给这个bean创建一个代理,当真正使用到这个bean时才会完全创建
     */
    @Autowired
    @Lazy
    private TokenStore tokenStore;
    @Value("${data.security.antMatchers}")
    private String excludeAntMatchers;
    @Autowired
    @Lazy
    private RedisTemplateUtils redisTemplateUtil;
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

       this.executeFilter(httpServletRequest, httpServletResponse, filterChain );
    }

    @Override
    public void destroy() {

    }

    private void executeFilter(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                          FilterChain filterChain) throws IOException, ServletException {

        String servletPath = httpServletRequest.getRequestURI();
        // 如果是OPTIONS则结束请求
        if (HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            return;
        }
        String accessToken = null;
        try {
            // 获取url携带的参数信息
            Map<String, Object> params = HttpClientUtils.getAllRequestParam(httpServletRequest);
            String curRequestURI = "当前访问的URL地址：" + servletPath + HttpClientUtils.paramsConvertUrl(params);
            accessToken = httpServletRequest.getParameter("access_token");
            String headerToken = httpServletRequest.getHeader(HEADER_AUTHORIZATION);
            if (StringUtils.isNotBlank(headerToken) && StringUtils.isBlank(accessToken)) {
                accessToken = headerToken.indexOf("bearer") != -1 ? headerToken.split(" ")[1] : "";
            }
            log.info(curRequestURI + "&access_token=" + accessToken);
            UserDetailsDto currentUser = null;
            if (StringUtils.isNotBlank(accessToken)) {
                // 从token中获取用户
                Object redisAuthentication = this.redisTemplateUtil.hget(BaseRedisKeys.USER_LOGIN_TOKNE, accessToken);
                currentUser = JSONObject.parseObject(redisAuthentication.toString(), UserDetailsDto.class);
                httpServletRequest.setAttribute(BaseRedisKeys.USER_ID, currentUser.getUserId());
                httpServletRequest.setAttribute(BaseRedisKeys.LESSEE, currentUser.getLessee());
                httpServletRequest.setAttribute(BaseRedisKeys.USER_INFO, JSON.toJSONString(currentUser));
            }
            //从request中解析PreAuthenticatedAuthenticationToken(注意这里并不是OAuth2Authentication)
            Authentication authentication = this.tokenExtractor.extract(httpServletRequest);
            // 不需要进行权限校验的url
            String[] antMatchers = excludeAntMatchers.trim().split(",");
            for (String matchers : antMatchers) {
                PathMatcher requestMatcher = new AntPathMatcher();
                boolean through = requestMatcher.match(matchers.trim(), servletPath);
                if (through) {
                    // this.setAuthentication(httpServletRequest, authentication, currentUser, accessToken);
                    //log.info(curRequestURI + " 不进行权限校验....");
                    filterChain.doFilter(httpServletRequest, httpServletResponse);
                    return;
                }
            }
            boolean legitimate = true;
            if (authentication == null) {
                if (this.stateless && this.isAuthenticated()) {
                    SecurityContextHolder.clearContext();
                }
                legitimate = false;
            } else {
                legitimate = this.setAuthentication(httpServletRequest, authentication, currentUser, accessToken);
            }
            if (!legitimate) {
                this.validationMessage(servletPath, accessToken, httpServletResponse);
                return;
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (Exception e) {
             e.printStackTrace();
             if (e instanceof InternalAuthenticationServiceException) {
                 this.validationMessage(servletPath, accessToken, httpServletResponse);
             } else {
                 throw new DescribeException(e.getMessage());
             }

        }
    }

    /**
     * 把 当前登录人 放入安全容器中 SecurityContextHolder
     * @param httpServletRequest
     * @param authentication
     * @param accessToken
     */
    private Boolean setAuthentication(HttpServletRequest httpServletRequest, Authentication authentication, UserDetailsDto currentUser, String accessToken) {
        boolean authenticated = false;
        if (StringUtils.isNotBlank(accessToken) && authentication != null ) {
            // log.info(" >>>>>开始验证token【" + accessToken + "】 是否有效 ");
            httpServletRequest.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, authentication.getPrincipal());
            OAuth2AccessToken oAuth2AccessToken = this.tokenStore.readAccessToken(accessToken);
            if (oAuth2AccessToken != null) {
                if(authentication instanceof AbstractAuthenticationToken) {
                    if (currentUser != null) {
                        // 当前登录人权限信息
                        Set<GrantedAuthority> grantedAuths = SecurityConstant.grantedAuths(currentUser.getAuthorities());
                        // 将当前登录人信息设置到 容器中
                        AbstractAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(currentUser.getUserAccounts(), currentUser.getSecret(), grantedAuths);
                        authRequest.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                                httpServletRequest));
                        Authentication authResult = this.authenticationManager
                                .authenticate(authRequest);
                        SecurityContext securityContext = SecurityContextHolder.getContext();
                        securityContext.setAuthentication(authResult);
                        authenticated = true;
                        TokenLocalContext.remove();
                        TokenLocalContext.setToken(accessToken);
                    }
                }
            }
        }
        return authenticated;
    }

    /**
     * 是否认证
     * @return
     */
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }

    /**
     * 用户认证失败提示信息
     * @param servletPath
     * @param accessToken
     * @param httpServletResponse
     */
    private void validationMessage(String servletPath, String accessToken, HttpServletResponse httpServletResponse) {
        Map<String, Object> map =  new HashMap<>();
        map.put("success", false);
        map.put("status", 401);
        map.put("path", servletPath);
        map.put("token", accessToken);
        map.put("message", "无效的token信息.");
        map.put("timestamp", String.valueOf(LocalDateTime.now()));
        log.info(JSONObject.toJSONString(map));
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        ResultUtil.writeJavaScript(httpServletResponse, map);
    }
}