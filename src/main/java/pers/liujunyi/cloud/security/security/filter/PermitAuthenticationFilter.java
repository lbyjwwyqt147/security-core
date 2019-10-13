package pers.liujunyi.cloud.security.security.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import pers.liujunyi.cloud.common.redis.RedisTemplateUtils;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.vo.BaseRedisKeys;
import pers.liujunyi.cloud.security.domain.user.UserDetailsDto;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;


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
    private TokenExtractor tokenExtractor = new BearerTokenExtractor();
    private boolean stateless = true;
    @Autowired
    private TokenStore tokenStore;
    @Value("${data.security.antMatchers}")
    private String excludeAntMatchers;
    @Autowired
    private RedisTemplateUtils redisTemplateUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info(" **************** 开始身份权限校验 ******************** ");
        String curUrl = httpServletRequest.getRequestURI();
        String curRequestURI = "当前访问的URL地址：" +curUrl;
        // 如果是OPTIONS则结束请求
        if (HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
            httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        String accessToken = httpServletRequest.getParameter("access_token");
        String headerToken = httpServletRequest.getHeader(HEADER_AUTHORIZATION);
        //从request中解析PreAuthenticatedAuthenticationToken(注意这里并不是OAuth2Authentication)
        Authentication authentication = this.tokenExtractor.extract(httpServletRequest);
        String[] antMatchers = excludeAntMatchers.trim().split(",");
        for (String matchers : antMatchers) {
            PathMatcher requestMatcher = new AntPathMatcher();
            boolean through = requestMatcher.match(matchers.trim(), curUrl);
            if (through) {
                this.setAuthentication(httpServletRequest, authentication, headerToken);
                log.info(curRequestURI + " 不进行权限校验....");
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
        }


        if (authentication == null) {
           /* if (this.stateless && this.isAuthenticated()) {
                // SecurityContextHolder.clearContext();
            }*/
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {

            Map<String, Object> map =  new HashMap<>();
            map.put("status", 401);
            AtomicBoolean error = new AtomicBoolean(false);
            if (StringUtils.isNotBlank(accessToken)) {
                try {
                    OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
                    if (oAuth2AccessToken == null) {
                        error.set(true);
                        map.put("message", " 无校的token信息.");
                        log.info(curRequestURI + " 无校的token信息.");
                    } else {
                        log.info( curRequestURI+" token:" + oAuth2AccessToken.getValue());
                    }
                } catch (InvalidTokenException e){
                    error.set(true);
                    map.put("message",e.getMessage());
                    log.info(curRequestURI + " 无校的token信息.");
                    // throw new AccessDeniedException("无校的token信息.");
                }

            } else if (StringUtils.isNotBlank(headerToken) && headerToken.startsWith(BEARER_AUTHENTICATION)){
                try {
                    OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(headerToken.split(" ")[0]);
                    if (oAuth2AccessToken == null) {
                        error.set(true);
                        map.put("message", " 无校的token信息.");
                        log.info(curRequestURI + " 无校的token信息.");
                    } else {
                        log.info( curRequestURI+" token:" + oAuth2AccessToken.getValue());
                    }
                } catch (InvalidTokenException e){
                    error.set(true);
                    map.put("message",e.getMessage());
                    log.info(curRequestURI + " 无校的token信息.");
                    // throw new AccessDeniedException("无校的token信息.");
                }

            } else {
                error.set(true);
                map.put("message", "参数无token.");
                log.info(curRequestURI + " 参数无token.");
                //throw new AccessDeniedException("参数无token.");
            }
            if (!error.get()){
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                map.put("path", httpServletRequest.getServletPath());
                map.put("timestamp", String.valueOf(LocalDateTime.now()));
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                ResultUtil.writeJavaScript(httpServletResponse, map);
            }
        }
    }

    /**
     * 把 当前登录人 放入安全容器中 SecurityContextHolder
     * @param httpServletRequest
     * @param authentication
     * @param accessToken
     */
    private void setAuthentication(HttpServletRequest httpServletRequest, Authentication authentication, String accessToken) {
        if (StringUtils.isNotBlank(accessToken) && authentication != null ) {
            String token = accessToken.indexOf("bearer") != -1 ? accessToken.split(" ")[1] : accessToken;
            if (StringUtils.isNotBlank(token)) {
                log.info(" >>>>>>>>>>> 　开始验证token是否有效　   ");
                httpServletRequest.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE, authentication.getPrincipal());
                OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
                if (oAuth2AccessToken != null) {
                    if(authentication instanceof AbstractAuthenticationToken) {
                        Object redisAuthentication = this.redisTemplateUtil.hget(BaseRedisKeys.USER_LOGIN_TOKNE, token);
                        UserDetailsDto userDetailsDto = JSONObject.parseObject(redisAuthentication.toString(), UserDetailsDto.class);
                        //Collection<? extends GrantedAuthority> authorities = JSON.parseObject(userDetailsDto.getAuthorities(), new TypeReference<Collection<? extends GrantedAuthority>>() {});
                        Set<GrantedAuthority> grantedAuths = new HashSet<>();
                        //模拟一个权限角色
                        //角色必须是ROLE_开头，可以在数据库中设置
                        grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                        AbstractAuthenticationToken currentUserAuthentication = new UsernamePasswordAuthenticationToken(userDetailsDto.getToken(), null, grantedAuths);
                        currentUserAuthentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(
                                httpServletRequest));
                        SecurityContextHolder.getContext().setAuthentication(currentUserAuthentication);
                    }
                }
            }
        }
    }

    /*public PermitAuthenticationFilter() {
        OAuth2AuthenticationManager oAuth2AuthenticationManager = new OAuth2AuthenticationManager();
        DefaultTokenServices dt = new DefaultTokenServices();
        dt.setTokenStore(tokenStore);
        oAuth2AuthenticationManager.setTokenServices(dt);
        this.setAuthenticationManager(oAuth2AuthenticationManager);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        log.info(" **************** 开始身份权限校验 ******************** ");
        String curRequestURI = "当前访问的URL地址：" + request.getRequestURI();
        try {
            // 如果是OPTIONS则结束请求
            if (HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                filterChain.doFilter(request, response);
                return;
            }
            Authentication authentication = this.tokenExtractor.extract(request);
            if (authentication == null) {
                if (this.stateless && this.isAuthenticated()) {
                    // SecurityContextHolder.clearContext();
                }
                log.info(curRequestURI + " 不进行拦截....");
                filterChain.doFilter(request, response);
            } else {
                log.info(" >>>>>>>>>>> 　开始验证token是否有效　   ");
                String accessToken = request.getParameter("access_token");
                String headerToken = request.getHeader(HEADER_AUTHORIZATION);
                Map<String, String> map =  new HashMap<>();
                map.put("status", "403");
                AtomicBoolean error = new AtomicBoolean(false);
                if (StringUtils.isNotBlank(accessToken)) {
                    try {
                        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessToken);
                        log.info( curRequestURI+" token:" + oAuth2AccessToken.getValue());
                    } catch (InvalidTokenException e){
                        error.set(true);
                        map.put("message",e.getMessage());
                        log.info(curRequestURI + " 无校的token信息.");
                        // throw new AccessDeniedException("无校的token信息.");
                    }

                } else if (StringUtils.isNotBlank(headerToken) && headerToken.startsWith(BEARER_AUTHENTICATION)){
                    try {
                        OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(headerToken.split(" ")[0]);
                        log.info(curRequestURI + " token:" + oAuth2AccessToken.getValue());
                    } catch (InvalidTokenException e){
                        error.set(true);
                        map.put("message",e.getMessage());
                        log.info(curRequestURI + " 无校的token信息.");
                        // throw new AccessDeniedException("无校的token信息.");
                    }

                } else {
                    error.set(true);
                    map.put("message", "参数无token.");
                    log.info(curRequestURI + " 参数无token.");
                    //throw new AccessDeniedException("参数无token.");
                }
                if (!error.get()){
                    filterChain.doFilter(request, response);
                } else {
                    map.put("path", request.getServletPath());
                    map.put("timestamp", String.valueOf(LocalDateTime.now()));
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    ResultUtil.writeJavaScript(response, map);
                }
            }
        } catch (Exception e) {
            throw new DescribeException(ErrorCodeEnum.ERROR);
        }

    }

    @Override
    public void destroy() {

    }

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken);
    }*/
}