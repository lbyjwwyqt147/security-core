package pers.liujunyi.cloud.security.security.filter;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

/***
 * 文件名称: CustomFilterSecurityInterceptor
 * 文件描述:  流程说明：首先，登陆后，每次访问资源都会被这个拦截器拦截，会执行doFilter这个方法，这个方法调用了invoke方法，其中fi断点显示是一个url
 *  *        最重要的是beforeInvocation这个方法，它首先会调用MyInvocationSecurityMetadataSource类的getAttributes方法获取被拦截url所需的权限
 *  *        在调用MyAccessDecisionManager类decide方法判断用户是否具有权限,执行完后就会执行下一个拦截器
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2020/5/29 11:35
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public class CustomFilterSecurityInterceptor extends FilterSecurityInterceptor {

    public CustomFilterSecurityInterceptor(FilterInvocationSecurityMetadataSource securityMetadataSource, AccessDecisionManager accessDecisionManager, AuthenticationManager authenticationManager){
        this.setSecurityMetadataSource(securityMetadataSource);
        this.setAccessDecisionManager(accessDecisionManager);
        this.setAuthenticationManager(authenticationManager);

    }
}
