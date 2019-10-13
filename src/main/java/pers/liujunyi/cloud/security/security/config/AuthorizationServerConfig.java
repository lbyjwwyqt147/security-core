package pers.liujunyi.cloud.security.security.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import pers.liujunyi.cloud.security.security.filter.PermitAuthenticationFilter;
import pers.liujunyi.cloud.security.security.token.RedisTokenStore;
import pers.liujunyi.cloud.security.util.SecurityConstant;

/***
 * 文件名称: AuthorizationServerConfig.java
 * 文件描述: Oauth2认证服务器
 * 公 司:
 * 内容摘要:
 * 其他说明: 配置客户端、token存储方式等
 *         EnableAuthorizationServer 注解开启验证服务器 提供/oauth/authorize,/oauth/token,/oauth/check_token,/oauth/confirm_access,/oauth/error
 * 完成日期:2019年10月08日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Configuration
@EnableAuthorizationServer
@Log4j2
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Autowired
    private MyUserDetailService myUserDetailService;
    @Autowired
    private PermitAuthenticationFilter permitAuthenticationFilter;

    /** 资源所有者（即用户）密码模式 */
    private static final String GRANT_TYPE_PASSWORD = "password";
    /** 授权码模式  授权码模式使用到了回调地址，是最为复杂的方式，通常网站中经常出现的微博，qq第三方登录，都会采用这个形式。 */
    private static final String AUTHORIZATION_CODE = "authorization_code";
    /** 获取access token时附带的用于刷新新的token模式 */
    private static final String REFRESH_TOKEN = "refresh_token";
    /** 简化授权模式 */
    private static final String IMPLICIT = "implicit";
    /** 客户端凭据（客户端ID以及Key）模式 */
    private static final String GRANT_TYPE = "client_credentials";
    private static final String SCOPE_READ = "read";
    private static final String SCOPE_WRITE = "write";
    private static final String TRUST = "trust";


    /**
     * OAuth2 token持久化到redis中
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(connectionFactory);
    }

    /**
     * 用来配置令牌端点(Token Endpoint)的安全约束.
     **/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // isAuthenticated():排除anonymous   isFullyAuthenticated():排除anonymous以及remember-me
        // allowFormAuthenticationForClients 允许表单认证
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();
        security.addTokenEndpointAuthenticationFilter(permitAuthenticationFilter);
    }

    /**
     * 配置客户端详情服务  本系统中使用客户端方式进行获取token
     * 可以把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息
     **/
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 用 BCrypt 对密码编码
        String secret = PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(SecurityConstant.CLIENT_SECRET);
        //下面配置3个个客户端,一个用于password认证、一个用于client认证、一个用于authorization_code认证
        // 使用in-memory存储
        clients.inMemory()
                // 设置客户端用户
                .withClient(SecurityConstant.CLIEN_ID)
                .resourceIds(SecurityConstant.RESOURCE_ID)
                //允许授权类型
                .authorizedGrantTypes(AUTHORIZATION_CODE, GRANT_TYPE, REFRESH_TOKEN, GRANT_TYPE_PASSWORD, IMPLICIT)
                //允许授权范围
                .scopes(SCOPE_READ, SCOPE_WRITE, TRUST)
                //客户端可以使用的权限
                .authorities("ROLE_CLIENT")
                //secret客户端安全码
                .secret(secret)
                //指定可以接受令牌和授权码的重定向URIs authorization_code认证方法用到
                //.redirectUris(REDIRECT_URL)
                // 为true 则不会被重定向到授权的页面，也不需要手动给请求授权,直接自动授权成功返回code
                .autoApprove(true)
                // 客户端的access_token的有效时间值(单位:秒)  若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时).
                .accessTokenValiditySeconds(SecurityConstant.ACCESS_TOKEN_VALIDITY_SECONDS)
                // 客户端的refresh_token的有效时间值(单位:秒)      若不设定值则使用默认的有效时间值(60 * 60 * 24 * 30, 30天).
                .refreshTokenValiditySeconds(SecurityConstant.REFRESH_TOKEN_VALIDITY_SECONDS)
                // scopes的值就是all（全部权限），read，write等权限。就是第三方访问资源的一个权限，访问范围。
                .scopes("all");
        log.info(" >>>>> Oauth2认证服务器 初始化完成. ");
    }

    /**
     * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
     **/
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                //自定义refresh_token刷新令牌对用户信息的检查，以确保用户信息仍然有效
                .userDetailsService(myUserDetailService)
                //通过authenticationManager开启密码授权
                .authenticationManager(authenticationManager)
                //支持GET  POST  请求获取token
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS)
                //refreshToken是否可以重复使用。 默认：true;
                .reuseRefreshTokens(true)
                //token相关服务
                .tokenServices(defaultTokenServices())
                //客户端信息
                .setClientDetailsService(endpoints.getClientDetailsService());
    }

    /**
     * <p>注意，自定义TokenServices的时候，需要设置@Primary，否则报错，</p>
     * 自定义的token
     * 认证的token是存到redis里的
     * @return
     */
    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices(){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        //token持久化容器
        tokenServices.setTokenStore(tokenStore());
        //是否复用refresh_token,默认为true(如果为false,则每次请求刷新都会删除旧的refresh_token,创建新的refresh_token)
        tokenServices.setReuseRefreshToken(true);
        //是否支持refresh_token，默认false
        tokenServices.setSupportRefreshToken(true);
        // token有效期自定义设置，默认12小时
        tokenServices.setAccessTokenValiditySeconds(SecurityConstant.ACCESS_TOKEN_VALIDITY_SECONDS);
        // refresh_token默认30天
        tokenServices.setRefreshTokenValiditySeconds(SecurityConstant.REFRESH_TOKEN_VALIDITY_SECONDS);
        return tokenServices;
    }

}

