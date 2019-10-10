package pers.liujunyi.cloud.security.security.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import pers.liujunyi.cloud.security.security.hander.CustomLoginFailHandler;

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
@Order(2)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Log4j2
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailService myUserDetailService;

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService)
                .passwordEncoder(bCryptPasswordEncoder());
        auth.parentAuthenticationManager(authenticationManagerBean());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 开启允许iframe 嵌套
       // http.headers().frameOptions().disable();
        http.csrf().disable()// 关闭跨站检测
                //.cors()  //开启跨域
               // .and()
                .authorizeRequests()   //authorizeRequests　配置权限　顺序为先配置需要放行的url 在配置需要权限的url，最后再配置.anyRequest().authenticated()
                .antMatchers("/oauth/**", "/api/v1/user/login", "/api/user/login", "/api/v1/out", "/api/v1/verify/ignore/**", "/api/v1/table/**").permitAll()   //无条件放行的资源
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // .antMatchers(excludeAntMatchers.split(",")).permitAll()   //无条件放行的资源
                .antMatchers("/api/**").authenticated()     //需要保护的资源
                .anyRequest().authenticated() //其他资源都受保护
                .and()
                .formLogin()
                .loginProcessingUrl("/api/user/login")  //指定登陆url  系统中我使用LoginController 中登录接口进行登录 /api/v1/user/login   不使用指定的 /api/user/login 登录接口  如果使用指定的 /api/user/login 登录请求触发后会直接进入MyUserDetailService中的MyUserDetailService方法 认证登录
                .loginPage("/api/v1/out")  //未登录时 页面跳转 这里返回json
                .passwordParameter("userPassword") // 指定密码参数名称（对应前端传给后天参数名）
                .usernameParameter("userAccount") // 指定账号参数名称（对应前端传给后天参数名）
                .failureHandler(new CustomLoginFailHandler())  //登陆失败处理类
                .permitAll();
        http.logout().permitAll();
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
                .antMatchers("/swagger-resources/**");

    }
}

