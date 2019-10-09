package pers.liujunyi.cloud.security.security.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
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

    /** 客户端 */
    private static final String CLIEN_ID = "client_photo";
    /** secret客户端安全码 */
    private static final String CLIENT_SECRET = "secret";
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
    /** 客户端的access_token的有效时间值(单位:秒) */
    private Integer accessTokenValiditySeconds = 60*60*12;
    /** 客户端的refresh_token的有效时间值(单位:秒) */
    private Integer refreshTokenValiditySeconds = 60 * 60 * 24 * 7;

    /**
     * @Description 使用TokenStore操作Token
     **/
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
        security.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()").allowFormAuthenticationForClients();;
    }

    /**
     * 配置客户端详情服务
     * 可以把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息
     **/
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        String secret = new BCryptPasswordEncoder().encode(CLIENT_SECRET);  // 用 BCrypt 对密码编码
        //配置3个个客户端,一个用于password认证、一个用于client认证、一个用于authorization_code认证
        clients.inMemory()  // 使用in-memory存储
                .withClient(CLIEN_ID)    //client_id用来标识客户的Id
                .resourceIds(SecurityConstant.RESOURCE_ID)
                .authorizedGrantTypes(AUTHORIZATION_CODE, GRANT_TYPE, REFRESH_TOKEN, GRANT_TYPE_PASSWORD, IMPLICIT)  //允许授权类型
                .scopes(SCOPE_READ, SCOPE_WRITE, TRUST)  //允许授权范围
                .authorities("ROLE_CLIENT")  //客户端可以使用的权限
                .secret(secret)  //secret客户端安全码
                //.redirectUris(REDIRECT_URL)  //指定可以接受令牌和授权码的重定向URIs
                .autoApprove(true) // 为true 则不会被重定向到授权的页面，也不需要手动给请求授权,直接自动授权成功返回code
                .accessTokenValiditySeconds(accessTokenValiditySeconds)   // 客户端的access_token的有效时间值(单位:秒)  若不设定值则使用默认的有效时间值(60 * 60 * 12, 12小时).
                .refreshTokenValiditySeconds(refreshTokenValiditySeconds) // 客户端的refresh_token的有效时间值(单位:秒)      若不设定值则使用默认的有效时间值(60 * 60 * 24 * 30, 30天).
                .scopes("all");  // scopes的值就是all（全部权限），read，write等权限。就是第三方访问资源的一个权限，访问范围。
        log.info(" >>>>> Oauth2认证服务器 初始化完成. ");

    }

    /**
     * 用来配置授权（authorization）以及令牌（token）的访问端点和令牌服务(token services)。
     **/
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                .userDetailsService(myUserDetailService)
                .authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS) //支持GET  POST  请求获取token
                .reuseRefreshTokens(true) //开启刷新token
                .tokenServices(defaultTokenServices());
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
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        // token有效期自定义设置，默认12小时
        tokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        // refresh_token默认30天
        tokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        return tokenServices;
    }

}

