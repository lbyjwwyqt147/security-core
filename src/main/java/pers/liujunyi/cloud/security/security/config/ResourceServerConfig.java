package pers.liujunyi.cloud.security.security.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.common.util.DateTimeUtils;
import pers.liujunyi.cloud.security.security.hander.*;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.HashMap;
import java.util.Map;

/***
 * 文件名称: ResourceServerConfig.java
 * 文件描述: 访问权限配置
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年10月08日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
@EnableResourceServer
@Order(3)
@Log4j2
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private DefaultTokenServices tokenServices;
    @Autowired
    private PermitAuthenticationFilter permitAuthenticationFilter;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(SecurityConstant.RESOURCE_ID).stateless(true).tokenServices(tokenServices);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {

        // 配置那些资源需要保护的
        http.authorizeRequests()   //authorizeRequests　配置权限　顺序为先配置需要放行的url 在配置需要权限的url，最后再配置.anyRequest().authenticated()
                .antMatchers("/oauth/**", "/user/login", "/api/v1/out", "/api/verify/ignore/**").permitAll()   //无条件放行的资源
                //.antMatchers(excludeAntMatchers.split(",")).permitAll()   //无条件放行的资源
                .antMatchers("/api/**").authenticated()     //需要保护的资源
                .anyRequest().authenticated()  //其他资源都受保护
                .and()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler())  //权限认证失败业务处理
                .authenticationEntryPoint(customAuthenticationEntryPoint())  //认证失败的业务处理
                .and()
                .formLogin()
                .loginProcessingUrl("/user/login")  //指定登陆url
                .successHandler(customLoginSuccessHandler())  //登陆成功处理类
                .failureHandler(customLoginFailHandler())  //登陆失败处理类
                .permitAll();
       // http.addFilterBefore(permitAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class); //自定义token过滤 token校验失败后自定义返回数据格式
        log.info(" >>>>> 访问权限配置 初始化完成. ");
      /*  http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .requestMatchers().antMatchers("/api/**")
                .and()
                .authorizeRequests()
                .antMatchers("/api/**").authenticated()
                .and()
                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler())  //权限认证失败业务处理
                .authenticationEntryPoint(customAuthenticationEntryPoint())  //认证失败的业务处理
                .and()
                .formLogin()
                .successHandler(customLoginSuccessHandler())  //登陆成功处理类
                .failureHandler(customLoginFailHandler())  //登陆失败处理类
                .permitAll();*/
    }

    /**
     * 注册退出成功　Bean
     * @return
     */
    @Bean
    public LogoutSuccessHandler customLogoutSuccessHandler(){
        return new CustomLogoutSuccessHandler();
    }


    /**
     * 注册登陆失败　Bean
     * @return
     */
    @Bean
    public AuthenticationFailureHandler customLoginFailHandler(){
        return new CustomLoginFailHandler();
    }

    /**
     * 注册登陆成功　Bean
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler customLoginSuccessHandler(){
        return new CustomLoginSuccessHandler();
    }


    /**
     * 注册身份认证失败　Bean
     * @return
     */
    @Bean
    public OAuth2AuthenticationEntryPoint customAuthenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

    /**
     * 注册权限认证失败　Bean
     * @return
     */
    @Bean
    public OAuth2AccessDeniedHandler customAccessDeniedHandler(){
        return new CustomAccessDenieHandler();
    }

    /**
     * 注册是否登录认证失败　Bean
     * @return
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomLoginUrlAuthenticationEntryPoint();
    }

    /**
     * 重写 token 验证失败后自定义返回数据格式
     * @return
     */
    @Bean
    public WebResponseExceptionTranslator webResponseExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity translate(Exception e) throws Exception {
                ResponseEntity responseEntity = super.translate(e);
                OAuth2Exception body = (OAuth2Exception) responseEntity.getBody();
                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
                // do something with header or response
                if(401 == responseEntity.getStatusCode().value()){
                    //自定义返回数据格式
                    Map<String, Object> map =  new HashMap<>();
                    map.put("status", ErrorCodeEnum.TOKEN_INVALID.getCode());
                    map.put("message", e.getMessage());
                    map.put("timestamp", DateTimeUtils.getCurrentDateTimeAsString());
                    map.put("description", ErrorCodeEnum.TOKEN_INVALID.getMessage());
                    return new ResponseEntity(JSON.toJSONString(map), headers, responseEntity.getStatusCode());
                } else {
                    return new ResponseEntity(body, headers, responseEntity.getStatusCode());
                }
            }
        };
    }
}

