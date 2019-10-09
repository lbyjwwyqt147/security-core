package pers.liujunyi.cloud.security.security.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.exception.DescribeException;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.repository.elasticsearch.user.UserAccountsElasticsearchRepository;

import java.util.HashSet;
import java.util.Set;


/***
 * 文件名称: MyUserDetailService.java
 * 文件描述: 自定义登录身份验证
 * 公 司:
 * 内容摘要:
 * 其他说明: 当用户登录时会进入此类的loadUserByUsername方法对用户进行验证，验证成功后会被保存在当前回话的principal对象中
 * 完成日期:2019年10月08日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
@Service(value = "myUserDetailService")
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserAccountsElasticsearchRepository userAccountsElasticsearchRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
       try {
           UserAccounts userAccounts = this.userAccountsElasticsearchRepository.findFirstByUserAccountsOrMobilePhoneOrUserNumber(userName, userName, userName);
           if (userAccounts == null) {
               log.info("登录用户【" + userName + "】不存在.");
               // throw new UsernameNotFoundException(userName);
               throw new DescribeException(ErrorCodeEnum.LOGIN_INCORRECT);
           }
           if (userAccounts.getUserStatus() == 1) {
               throw new DescribeException(ErrorCodeEnum.USER_LOCK);
           }
           // 可用性 :true:可用 false:不可用
           boolean enabled = true;
           // 过期性 :true:没过期 false:过期
           boolean accountNonExpired = true;
           // 有效性 :true:凭证有效 false:凭证无效
           boolean credentialsNonExpired = true;
           // 锁定性 :true:未锁定 false:已锁定
           boolean accountNonLocked = true;
           Set<GrantedAuthority> grantedAuthorities = this.getAuthority(userAccounts);

           User user = new User(userAccounts.getUserAccounts(), userAccounts.getUserPassword(),
                   enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
           return user;
       } catch (Exception e) {
           throw new DescribeException(ErrorCodeEnum.LOGIN_INCORRECT);
       }

    }


    /**
     * 用户拥有的权限角色
     * @return
     */
    private Set<GrantedAuthority> getAuthority(UserAccounts userAccounts) {
        Set<GrantedAuthority> grantedAuths = new HashSet<>();
        //模拟一个权限角色
        //角色必须是ROLE_开头，可以在数据库中设置
        grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
         /*for (Role role : member.getRoles()) {
            //角色必须是ROLE_开头，可以在数据库中设置
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
            grantedAuthorities.add(grantedAuthority);
            //获取权限
            for (Permission permission : role.getPermissions()) {
                GrantedAuthority authority = new SimpleGrantedAuthority(permission.getUri());
                grantedAuthorities.add(authority);
            }
        }*/
        return grantedAuths;
    }
}

