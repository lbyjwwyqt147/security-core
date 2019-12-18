package pers.liujunyi.cloud.security.security.filter;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import pers.liujunyi.cloud.security.entity.authorization.MenuResource;
import pers.liujunyi.cloud.security.entity.authorization.RoleInfo;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleInfoMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.RoleResourceMongoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/***
 * 文件名称: CustomInvocationSecurityMetadataSource.java
 * 文件描述: 加载资源与权限的对应关系
 * 公 司:
 * 内容摘要:
 * 其他说明:　
 *         实现FilterInvocationSecurityMetadataSource接口也是必须的。 首先，这里从数据库中获取信息。 其中loadResourceDefine方法不是必须的，
 *         这个只是加载所有的资源与权限的对应关系并缓存起来，避免每次获取权限都访问数据库（提高性能），然后getAttributes根据参数（被拦截url）返回权限集合。
 *         这种缓存的实现其实有一个缺点，因为loadResourceDefine方法是放在构造器上调用的，而这个类的实例化只在web服务器启动时调用一次，那就是说loadResourceDefine方法只会调用一次，
 *         如果资源和权限的对应关系在启动后发生了改变，那么缓存起来的权限数据就和实际授权数据不一致，那就会授权错误了。但如果资源和权限对应关系是不会改变的，这种方法性能会好很多。
 *         要想解决 权限数据的一致性 可以直接在getAttributes方法里面调用数据库操作获取权限数据，通过被拦截url获取数据库中的所有权限，封装成Collection<ConfigAttribute>返回就行了。（灵活、简单
 *         器启动加载顺序：1：调用loadResourceDefine()方法  2：调用supports()方法   3：调用getAllConfigAttributes()方法
 * 完成日期:2018年10月13日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
@Component
public class CustomInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    /** 存放资源配置对象 */
    public static Map<String, Collection<ConfigAttribute>> resourceMap = null;
    @Autowired
    private RoleInfoMongoRepository roleInfoMongoRepository;
    @Autowired
    private RoleResourceMongoService roleResourceMongoService;

    /**
     * 参数是要访问的url，返回这个url对于的所有权限（或角色）
     * 每次请求后台就会调用 得到请求所拥有的权限
     * 这个方法在url请求时才会调用，服务器启动时不会执行这个方法
     * getAttributes这个方法会根据你的请求路径去获取这个路径应该是有哪些权限才可以去访问。
     *
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //取消这段代码注释 情况下 每次服务启动后请求后台只有到数据库中取一次权限   如果注释掉这段代码则每次请求都会到数据库中取权限
        if (resourceMap == null){
            // 每次请求 都会去数据库查询权限  貌似很耗性能
             loadResourceDefine();
        }
        // object 是一个URL，被用户请求的url。
        FilterInvocation invocation = (FilterInvocation) object;
        String url = invocation.getRequestUrl();
        int firstQuestionMarkIndex = url.indexOf("?");
        if (firstQuestionMarkIndex != -1) {
            url = url.substring(0, firstQuestionMarkIndex);
        }
        // 路径支持Ant风格的通配符 /spitters/**
        PathMatcher requestMatcher = new AntPathMatcher();
        //循环已有的角色配置对象 进行url匹配
        Iterator<String> ite = resourceMap.keySet().iterator();
        while (ite.hasNext()) {
            String resURL = ite.next().trim();
            boolean through = requestMatcher.match(resURL.trim(), url);
            if (through) {
                return resourceMap.get(resURL);
            }
        }
        return null ;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        //要返回true  不然要报异常　　 SecurityMetadataSource does not support secure object class: class
        return true;
    }


    /**
     * 初始化资源 ,提取系统中的所有权限，加载所有url和权限（或角色）的对应关系， 以便拦截无权放访问的用户请求。  web容器启动就会执行
     */
    public void loadResourceDefine() {
        if (resourceMap == null) {
            //应当是资源为key， 权限为value。 资源通常为url， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
            resourceMap = new ConcurrentHashMap<>();
            String prefix = SecurityConstant.ROLE_PREFIX;
            // 获取系统中的所有角色信息
            List<RoleInfo> roleList = this.roleInfoMongoRepository.findAll();
            if (!CollectionUtils.isEmpty(roleList)) {
                List<Long> roleIds = roleList.stream().map(RoleInfo::getId).collect(Collectors.toList());
                // 获取角色分配的的功能按钮资源
                Map<Long, List<MenuResource>> buttonResourceMap = this.roleResourceMongoService.getResourceByRoleIdIn(roleIds, (byte)3);
                roleList.stream().forEach(role -> {
                    if (StringUtils.isNotBlank(role.getRoleAuthorizationCode())) {
                        //角色授权码
                        String authorizedCode = prefix + role.getRoleAuthorizationCode().trim().toUpperCase();
                        ConfigAttribute configAttributes = new SecurityConfig(authorizedCode);
                        List<MenuResource> resourceList = buttonResourceMap.get(role.getId());
                        if (!CollectionUtils.isEmpty(resourceList)) {
                            List<String> urlPatch = resourceList.stream().filter(r -> StringUtils.isNotBlank(r.getMenuPath())).map(MenuResource::getMenuPath).distinct().collect(Collectors.toList());
                            urlPatch.stream().forEach(item -> {
                                // 判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，将权限增加到权限集合中。
                                if (resourceMap.containsKey(item)) {
                                    Collection<ConfigAttribute> value = resourceMap.get(item);
                                    value.add(configAttributes);
                                    resourceMap.put(item, value);
                                } else {
                                    Collection<ConfigAttribute> atts = new ArrayList<>();
                                    atts.add(configAttributes);
                                    resourceMap.put(item, atts);
                                }
                            });
                        }
                    }
                });
            }
        }
    }
}
