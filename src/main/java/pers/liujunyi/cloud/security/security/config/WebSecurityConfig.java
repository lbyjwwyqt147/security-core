package pers.liujunyi.cloud.security.security.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsUtils;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.common.util.DateTimeUtils;
import pers.liujunyi.cloud.security.security.filter.PermitAuthenticationFilter;
import pers.liujunyi.cloud.security.security.hander.CustomAccessDenieHandler;
import pers.liujunyi.cloud.security.security.hander.CustomAuthenticationEntryPoint;
import pers.liujunyi.cloud.security.security.hander.CustomLoginFailHandler;
import pers.liujunyi.cloud.security.security.hander.CustomLoginSuccessHandler;

import java.util.HashMap;
import java.util.Map;

/***
 * 文件名称: WebSecurityConfig.java
 * 文件描述: 配置Spring Security
 * 公 司:
 * 内容摘要:
 * 其他说明: ResourceServerConfig 是比SecurityConfig 的优先级低的
 * 完成日期:2019年10月08日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
@EnableWebSecurity
@Order(1)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailService myUserDetailService;
    @Value("${data.security.antMatchers}")
    private String excludeAntMatchers;
    @Autowired
    private PermitAuthenticationFilter permitAuthenticationFilter;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService)
                .passwordEncoder(bCryptPasswordEncoder());
        auth.parentAuthenticationManager(authenticationManagerBean());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] tempAntMatchers = excludeAntMatchers.trim().split(",");
        int length =  tempAntMatchers.length;
        String[] antMatchers = new String[length];
        for (int i = 0; i < length; i++) {
            antMatchers[i] = tempAntMatchers[i].trim();
        }
        // 开启跨域
        http.cors()
                .and()
                // 关闭跨站检测
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                //authorizeRequests　配置权限　顺序为先配置需要放行的url 在配置需要权限的url，最后再配置.anyRequest().authenticated()
                .authorizeRequests()
                //处理跨域请求中的Preflight请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .and().authorizeRequests()
                //无条件放行的资源
                //.antMatchers("/oauth/**", "/api/v1/user/login", "/api/user/login", "/api/v1/out", "/api/v1/verify/ignore/**", "/api/v1/table/**").permitAll()
                //无条件放行的资源
                .antMatchers(antMatchers).permitAll()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .and().authorizeRequests()
                //需要身份证认证保护的资源
                .antMatchers("/api/**").authenticated()
                .and().authorizeRequests()
                //其他资源都受保护
                .anyRequest().authenticated()
                .and()
                .anonymous().disable()
                .exceptionHandling()
                //权限认证失败业务处理
                .accessDeniedHandler(customAccessDeniedHandler())
                //身份认证失败的业务处理
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .and()
                .formLogin()
                //指定登陆url  系统中我使用LoginController 中登录接口进行登录 /api/v1/user/login   不使用指定的 /api/user/login 登录接口  如果使用指定的 /api/user/login 登录请求触发后会直接进入MyUserDetailService中的MyUserDetailService方法 认证登录
                // 系统中由于使用LoginController 中登录接口 所以不会触发successHandler和failureHandler
                .loginProcessingUrl("/api/user/login")
                //未登录时 页面跳转 这里返回json
                .loginPage("/api/v1/out")
                // 指定密码参数名称（对应前端传给后台参数名）
                .passwordParameter("userPassword")
                // 指定账号参数名称（对应前端传给后台参数名）
                .usernameParameter("userAccount")
                //登陆成功处理类
                .successHandler(customLoginSuccessHandler())
                //登陆失败处理类
                .failureHandler(customLoginFailHandler());
        http.logout().permitAll();
        // 禁用缓存
        http.headers().cacheControl();
        // 添加 filter 验证其他请求的Token是否合法
        http.addFilterBefore(permitAuthenticationFilter, FilterSecurityInterceptor.class);
        // 加入自定义UsernamePasswordAuthenticationFilter替代原有Filter
      //  http.addFilterAt(customUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        log.info(" >>>>> SecurityConfig 初始化完成. ");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService).passwordEncoder(bCryptPasswordEncoder());
        auth.parentAuthenticationManager(authenticationManagerBean());
    }

    /**
     * 不定义没有password grant_type,密码模式需要AuthenticationManager支持
     *
     * @return
     * @throws Exception
     */
    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 加密方式
     * @return
     */
    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring(). antMatchers("/swagger-ui.html")
                .antMatchers("/webjars/**")
                .antMatchers("/v2/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/favicon.ico")
                .antMatchers( "/error");
        super.configure(web);
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

