package pers.liujunyi.cloud.security.security.filter;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import javax.servlet.*;
import java.io.IOException;

/***
 * 文件名称: CustomSecurityInterceptor.java
 * 文件描述: 过滤用户请求 （废弃）
 * 公 司:
 * 内容摘要:
 * 其他说明:　
 *         继承AbstractSecurityInterceptor、实现Filter是必须的
 *         首先，登陆后，每次访问资源都会被这个拦截器拦截，会执行doFilter这个方法，这个方法调用了invoke方法，其中fi断点显示是一个url
 *         最重要的是beforeInvocation这个方法，它首先会调用MyInvocationSecurityMetadataSource类的getAttributes方法获取被拦截url所需的权限
 *         在调用MyAccessDecisionManager类decide方法判断用户是否具有权限,执行完后就会执行下一个拦截器
 *         注意要注入权限类FilterInvocationSecurityMetadataSource和setAccessDecisionManager(AccessDecisionManager)
 * 完成日期:2018年10月13日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
@Deprecated
//@Component
public class CustomSecurityInterceptor extends AbstractSecurityInterceptor implements Filter{

    @Autowired
    private FilterInvocationSecurityMetadataSource customInvocationSecurityMetadataSource;



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 必须设置
     * @param customAccessDecisionManager
     */
    @Autowired
    @Override
    public void setAccessDecisionManager(AccessDecisionManager customAccessDecisionManager) {
        super.setAccessDecisionManager(customAccessDecisionManager);
    }

    /**
     * 登录后 每次请求都会调用这个拦截器进行请求过滤
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        invoke(fi);
    }

    @Override
    public void destroy() {

    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.customInvocationSecurityMetadataSource;
    }


    /**
     * 拦截请求处理
     * @param fi
     * @throws IOException
     * @throws ServletException
     */
    public void invoke(FilterInvocation fi) throws IOException, ServletException {
        //fi里面有一个被拦截的url
        //里面调用MyInvocationSecurityMetadataSource的getAttributes(Object object)这个方法获取fi对应的所有权限
        //再调用MyAccessDecisionManager的decide方法来校验用户的权限是否足够
        InterceptorStatusToken token = super.beforeInvocation(fi);
        try {
            //执行下一个拦截器
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }
}
