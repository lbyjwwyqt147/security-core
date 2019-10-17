package pers.liujunyi.cloud.security.security.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.web.cors.CorsUtils;
import pers.liujunyi.cloud.security.security.filter.PermitAuthenticationFilter;
import pers.liujunyi.cloud.security.util.SecurityConstant;

/***
 * 文件名称: ResourceServerConfig.java
 * 文件描述: 资源服务器，保护受保护的资源(保护受保护的资源(Spring Security auth 的http 资源拦截和保护))
 * 公 司:
 * 内容摘要:
 * 其他说明:  ResourceServerConfigurerAdapter是默认情况下spring security oauth2的http配置 
 *          在ResourceServerProperties中，定义了它的order默认值为SecurityProperties.ACCESS_OVERRIDE_ORDER - 1;是大于1的,
 *          即WebSecurityConfigurerAdapter的配置的拦截要优先于ResourceServerConfigurerAdapter，优先级高的http配置是可以覆盖优先级低的配置的。
 * 完成日期:2019年10月08日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
@EnableResourceServer
@Log4j2
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private DefaultTokenServices tokenServices;
    @Autowired
    private PermitAuthenticationFilter permitAuthenticationFilter;
    @Autowired
    private OAuth2AccessDeniedHandler customAccessDeniedHandler;
    @Autowired
    private OAuth2AuthenticationEntryPoint customAuthenticationEntryPoint;
    @Value("${data.security.antMatchers}")
    private String excludeAntMatchers;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(SecurityConstant.RESOURCE_ID).stateless(true).tokenServices(tokenServices);
        //自定义资源访问认证异常，没有token，或token错误，使用customAuthenticationEntryPoint
        resources.authenticationEntryPoint(customAuthenticationEntryPoint);
        resources.accessDeniedHandler(customAccessDeniedHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //关闭跨站请求防护
        http.cors().and().csrf().disable();
        // 资源保护配置
        // http.antMatcher表明这个 HttpSecurity 只适用于以 /api/开头的URL。
        //antMatcher()``是HttpSecurity的一个方法，他只告诉了Spring我只配置了一个我这个Adapter能处理哪个的url，它与authorizeRequests()没有任何关系。
        http.antMatcher("/api/**").authorizeRequests()
                //处理跨域请求中的Preflight请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // 配置不需要权限认证的资源
                .antMatchers(SecurityConstant.antMatchers(excludeAntMatchers)).permitAll()
                // 其他资源都需要保护
                .anyRequest().authenticated();

        // 资源异常信息处理
        http.exceptionHandling()
                //权限认证失败业务处理
                .accessDeniedHandler(customAccessDeniedHandler)
                //身份认证失败的业务处理
                .authenticationEntryPoint(customAuthenticationEntryPoint);

        http.formLogin().permitAll();
        http.httpBasic();
        // OAuth2受保护资源的预认证过滤器
        http.addFilterBefore(permitAuthenticationFilter, AbstractPreAuthenticatedProcessingFilter.class);
        log.info(" >>>>> ResourceServerConfig 资源服务器(保护受保护的资源) 初始化完成. ");
    }


}

