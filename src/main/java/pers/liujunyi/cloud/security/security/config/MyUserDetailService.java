package pers.liujunyi.cloud.security.security.config;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.exception.DescribeException;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.common.util.SecurityLocalContext;
import pers.liujunyi.cloud.security.entity.authorization.RoleInfo;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.repository.mongo.user.UserAccountsMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.RoleUserMongoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/***
 * 文件名称: MyUserDetailService.java
 * 文件描述: 自定义登录身份验证
 * 公 司:
 * 内容摘要:
 * 其他说明: 当用户登录时会进入此类的loadUserByUsername方法对用户进行验证，验证成功后会被保存在当前回话的principal对象中
 *          系统获取当前登录对象信息方法 User User = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 * 完成日期:2019年10月08日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Log4j2
@Service(value = "myUserDetailService")
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private UserAccountsMongoRepository userAccountsMongoRepository;
    @Autowired
    private RoleUserMongoService roleUserMongoService;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
           UserAccounts userAccounts = this.userAccountsMongoRepository.findFirstByUserAccountsOrMobilePhoneOrUserNumber(userName, userName, userName);
           if (userAccounts == null) {
               log.info("登录用户【" + userName + "】不存在.");
               throw new DescribeException(ErrorCodeEnum.LOGIN_INCORRECT);
           }

           // 可用性 :true:可用 false:不可用
           boolean enabled = true;
           // 过期性 :true:没过期 false:过期
           boolean accountNonExpired = true;
           // 有效性 :true:凭证有效 false:凭证无效
           boolean credentialsNonExpired = true;
           // 锁定性 :true:未锁定 false:已锁定
           boolean accountNonLocked = true;
           if (userAccounts.getUserStatus() == 1) {
               accountNonLocked = false;
           }
           Set<GrantedAuthority> grantedAuthorities = this.getAuthority(userAccounts);

           User user = new User(userName, userAccounts.getUserPassword(),
                   enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, grantedAuthorities);
           // 设置当前登录人的角色权限授权码
           SecurityLocalContext.remove();
           int authSize = grantedAuthorities.size();
           String[] authorities = new String[authSize];
           int i = 0;
           Iterator<GrantedAuthority> it = grantedAuthorities.iterator();
           while (it.hasNext()) {
               GrantedAuthority grantedAuthority = it.next();
               authorities[i] = grantedAuthority.getAuthority();
               i++;
           }
           SecurityLocalContext.setAuthorities(authorities);
           return user;

    }


    /**
     * 用户拥有的权限角色
     * @return
     */
    private Set<GrantedAuthority> getAuthority(UserAccounts userAccounts) {
        Set<GrantedAuthority> grantedAuths = new HashSet<>();
        String prefix = SecurityConstant.ROLE_PREFIX;
        //模拟一个权限角色  角色必须是ROLE_开头
        grantedAuths.add(new SimpleGrantedAuthority(SecurityConstant.RESOURCE_AUTHORITIES));
        // 获取用户拥有的角色
        List<RoleInfo> roleInfoList = this.roleUserMongoService.getRoleInfoByUserId(userAccounts.getId());
        if (!CollectionUtils.isEmpty(roleInfoList)) {
            roleInfoList.stream().forEach(role -> {
                if (StringUtils.isNotBlank(role.getRoleAuthorizationCode())) {
                    //角色授权码
                    String authorizedCode = prefix + role.getRoleAuthorizationCode().trim().toUpperCase();
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authorizedCode);
                    grantedAuths.add(grantedAuthority);
                }
            });
        }
        return grantedAuths;
    }
}

