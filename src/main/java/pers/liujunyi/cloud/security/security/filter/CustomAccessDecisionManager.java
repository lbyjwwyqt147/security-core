package pers.liujunyi.cloud.security.security.filter;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.common.util.SecurityAuthoritiesLocalContext;

import java.util.Collection;
import java.util.Iterator;

/***
 * 文件名称: CustomAccessDecisionManager.java
 * 文件描述: 资源权限认证器  证用户是否拥有所请求资源的权限
 * 公 司:
 * 内容摘要:
 * 其他说明:　
 *         接口AccessDecisionManager也是必须实现的。 decide方法里面写的就是授权策略了，需要什么策略，可以自己写其中的策略逻辑
 *         认证通过就返回，不通过抛异常就行了，spring security会自动跳到权限不足处理类（WebSecurityConfig 类中 配置文件上配的）
 * 完成日期:2018年10月13日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
@Component
public class CustomAccessDecisionManager implements AccessDecisionManager {

    /**
     *  授权策略
     *
     * decide()方法在url请求时才会调用，服务器启动时不会执行这个方法
     *
     * @param configAttributes 装载了请求的url允许的角色数组 。这里是从MyInvocationSecurityMetadataSource里的loadResourceDefine方法里的atts对象取出的角色数据赋予给了configAttributes对象
     * @param object url
     * @param authentication 装载了从数据库读出来的权限(角色) 数据。如果使用OAuth2 则 读取的是 AuthorizationServerConfig 类中configure方法配置的.authorities（客户端允许访问的权限）,这里是从MyUserDetailService里的loadUserByUsername方法里的grantedAuths对象的值传过来给 authentication 对象,简单点就是从spring的全局缓存SecurityContextHolder中拿到的，里面是用户的权限信息
     *
     * 注意： Authentication authentication 如果是前后端分离 则有跨域问题，跨域情况下 authentication 无法获取当前登陆人的身份认证(登陆成功后)，我尝试用token来效验权限
     *
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        Object principal = authentication.getPrincipal();
        if (principal instanceof String ) {
            //通过 oauth 获取的token，直接允许通过
            return;
        }
        // 判断有无权限访问
        if(!CollectionUtils.isEmpty(configAttributes)){
            // authentication 返回的权限信息是 AuthorizationServerConfig 类中configure方法配置的.authorities（客户端允许访问的权限）
            // 例如 这里配置的是  .authorities("ROLE_CLIENT") 权限  则这里取出来的权限值是 ROLE_CLIENT  而不是从当前登录人的设置的权限，不知道那里出了问题，未解决这个问题
           // authentication = SecurityContextHolder.getContext().getAuthentication();
            Iterator<ConfigAttribute> iterator = configAttributes.iterator();
            while (iterator.hasNext()){
                ConfigAttribute configAttribute = iterator.next();
                String needRole = configAttribute.getAttribute();
                // 为了解决 当前 Authentication authentication 无法获取当前登录人的角色权限信息，我把当前登录人的角色权限授权码 设置在了ThreadLocal 里面（不知这种方式是否正确）
                String[] authorities = SecurityAuthoritiesLocalContext.getAuthorities();
                if (StringUtils.isNotBlank(needRole) && authorities != null) {
                    for(String authoritie : authorities){
                        //authoritie 为用户所被赋予的权限。 needRole 为访问相应的资源应该具有的权限。
                        //判断两个请求的url的权限和用户具有的权限是否相同，如相同，允许访问 权限就是那些以ROLE_为前缀的角色
                        if (needRole.trim().equals(authoritie.trim())){
                            //匹配到对应的角色，则允许通过
                            return;
                        }
                    }
                }
            }
        }
        // 抛出当前资源无权限访问异常
        throw new AccessDeniedException(ErrorCodeEnum.AUTHORITY.getMessage());
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

}
