package pers.liujunyi.cloud.security.controller.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.annotation.ControllerMethodLog;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.encrypt.annotation.Decrypt;
import pers.liujunyi.cloud.common.encrypt.annotation.Encrypt;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.common.redis.RedisTemplateUtils;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.common.util.HttpClientUtils;
import pers.liujunyi.cloud.common.util.SecurityLocalContext;
import pers.liujunyi.cloud.common.util.TokenLocalContext;
import pers.liujunyi.cloud.common.vo.BaseRedisKeys;
import pers.liujunyi.cloud.common.vo.user.UserDetails;
import pers.liujunyi.cloud.security.domain.user.LoginDto;
import pers.liujunyi.cloud.security.domain.user.UserDetailsDto;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.repository.mongo.user.UserAccountsMongoRepository;
import pers.liujunyi.cloud.security.security.filter.CustomInvocationSecurityMetadataSource;
import pers.liujunyi.cloud.security.service.user.UserAccountsService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: LoginController.java
 * 文件描述: 登录 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年10月08日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "登录 API")
@RestController
@Log4j2
public class LoginController extends BaseController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;
    @Value("${server.port}")
    private Integer curPort;
    @Autowired
    private RedisTemplateUtils redisTemplateUtil;
    @Autowired
    private UserAccountsMongoRepository userAccountsMongoRepository;
    @Autowired
    private UserAccountsService userAccountsService;
    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private TokenStore tokenStore;

    /**
     * 退出
     * @param
     * @return
     */
    @GetMapping(value = "out")
    public ResultInfo timeOut() {
        return ResultUtil.info(504, "你尚未登录,请登录.", null, true);
    }

    /**
     * 用户登陆
     * @param loginDto
     * @return
     */
    @ControllerMethodLog(desc = "用户登陆", operModule = "登录", logType = 3, serviceClass = UserAccountsService.class, entityBeanClass = UserAccounts.class)
    @ApiOperation(value = "用户登陆")
    @PostMapping(value = "user/login")
    @ApiVersion(1)
    @Encrypt
    @Decrypt
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1")
    })
    public ResultInfo userLogin(@Valid @RequestBody LoginDto loginDto) {

        //登录 身份认证
        // 这句代码会自动执行咱们自定义的 "MyUserDetailService.java" 身份认证类
        //1: 将用户名和密码封装成UsernamePasswordAuthenticationToken  new UsernamePasswordAuthenticationToken(userAccount, userPwd)
        //2: 将UsernamePasswordAuthenticationToken传给AuthenticationManager进行身份认证   authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAccount, userPwd));
        //3: 认证完毕，返回一个认证后的身份： Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userAccount, userPwd));
        //4: 认证后，存储到SecurityContext里   SecurityContext securityContext = SecurityContextHolder.getContext();securityContext.setAuthentication(authentication);


        //UsernamePasswordAuthenticationToken继承AbstractAuthenticationToken实现Authentication
        //当在页面中输入用户名和密码之后首先会进入到UsernamePasswordAuthenticationToken验证(Authentication)，注意用户名和登录名都是页面传来的值
        //然后生成的Authentication会被交由AuthenticationManager来进行管理
        //而AuthenticationManager管理一系列的AuthenticationProvider，
        //而每一个Provider都会通UserDetailsService和UserDetail来返回一个
        //以UsernamePasswordAuthenticationToken实现的带用户名和密码以及权限的Authentication
        try {
            UserAccounts accounts = this.userAccountsMongoRepository.findFirstByUserAccountsOrMobilePhoneOrUserNumber(loginDto.getUserAccount(), loginDto.getUserAccount(), loginDto.getUserAccount());
            if (accounts == null || !bCryptPasswordEncoder.matches(loginDto.getUserPassword(), accounts.getUserPassword())) {
                return ResultUtil.fail("用户或者密码错误.");
            }
            if (accounts.getUserStatus() == 1) {
                return ResultUtil.info(ErrorCodeEnum.USER_LOCK.getCode(), ErrorCodeEnum.USER_LOCK.getMessage(), null, false);
            }
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserAccount(), loginDto.getUserPassword()));
            //将身份 存储到SecurityContext里
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            // 这个非常重要(非前后端分离情况下)，否则验证后将无法登陆
            // request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            request.setAttribute(BaseRedisKeys.USER_ID, accounts.getId());
            request.setAttribute(BaseRedisKeys.LESSEE, accounts.getTenementId());
            String token = this.decodeToken();
          //  this.clientToken(authentication);
            log.info("当前登录人【" + loginDto.getUserAccount() + "】的token:" + token);
            UserDetails userDetails = DozerBeanMapperUtil.copyProperties(accounts, UserDetails.class);
            userDetails.setUserId(accounts.getId());
            userDetails.setToken(token);
            UserDetailsDto detailsDto = DozerBeanMapperUtil.copyProperties(userDetails, UserDetailsDto.class);
            detailsDto.setAuthorities(JSONObject.toJSONString(authentication.getAuthorities()));
            detailsDto.setAuthenticated(true);
            detailsDto.setCredentials(authentication.getCredentials());
            detailsDto.setPrincipal(authentication.getPrincipal());
            detailsDto.setSecret(loginDto.getUserPassword());
            request.setAttribute(BaseRedisKeys.USER_INFO,  JSON.toJSONString(userDetails));
            this.saveUserToRedis(token, detailsDto);
            // 设置登录时间
            this.userAccountsService.setLoginTimeById(new Date(), accounts.getLoginTime(), accounts.getLoginCount(), accounts.getId(), accounts.getDataVersion());

            return ResultUtil.success(userDetails, token);
        } catch (AuthenticationException e){
            e.printStackTrace();
            return ResultUtil.fail("用户或者密码错误.");
        }
    }

    /**
     * 获取当前登录用户信息
     * @param principal
     * @return
     */
    @ApiOperation(value = "获取当前登录用户信息")
    @GetMapping(value = "ignore/user/info")
    public Principal user(Principal principal) {
        //获取当前用户信息
        log.debug("user", principal);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug(authentication);
        return principal;
    }



    /**
     * 用户登录退出
     * @param access_token
     * @return
     */
    @ControllerMethodLog(desc = "用户登录退出", operModule = "登录", logType = 4, serviceClass = UserAccountsService.class, entityBeanClass = UserAccounts.class)
    @ApiOperation(value = "用户登录退出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1"),
            @ApiImplicitParam(name = "access_token", value = "access_token",  required = true, dataType = "String"),
    })
    @DeleteMapping(value = "user/out")
    @ApiVersion(1)
    public ResultInfo revokeToken(String access_token, HttpServletRequest request) {
        //注销当前用户
        String token = access_token.trim();
        if (consumerTokenServices.revokeToken(token)) {
            this.redisTemplateUtil.hdel(BaseRedisKeys.USER_LOGIN_TOKNE, token);
            this.redisTemplateUtil.hdel(BaseRedisKeys.USER_AUTHORITIES_TOKEN, token);
            request.removeAttribute(BaseRedisKeys.USER_INFO);
            request.removeAttribute(BaseRedisKeys.LESSEE);
            request.removeAttribute(BaseRedisKeys.USER_ID);
            request.removeAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE);
            TokenLocalContext.remove();
            CustomInvocationSecurityMetadataSource.resourceMap = null;
            SecurityLocalContext.remove();
        }
        return ResultUtil.success();
    }

    /**
     * 获取 登录token 数据
     */
    private String decodeToken() {
        String token = null;
        String url = "http://127.0.0.1:" + curPort + "/oauth/token";
        Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("grant_type", "client_credentials");
        params.put("scope", "all");
        params.put("client_id", SecurityConstant.CLIEN_ID);
        params.put("client_secret", SecurityConstant.CLIENT_SECRET);
        String tokenJson = HttpClientUtils.httpPost(url, params);
        JSONObject jsonObject = JSONObject.parseObject(tokenJson);
        return jsonObject.getString("access_token");
    }

    /**
     * 获取 登录token 数据
     */
    private String clientToken(Authentication authentication) {
        String token = null;
        //获取clientId 和 clientSecret
        String clientId = SecurityConstant.CLIEN_ID;
        String clientSecret = SecurityConstant.CLIENT_SECRET;
        //获取 ClientDetails
        ClientDetails clientDetails = this.clientDetailsService.loadClientByClientId(clientId);
        if (clientDetails == null){
            throw new UnapprovedClientAuthenticationException("clientId 不存在" + clientId);
        }else if (!bCryptPasswordEncoder.matches(clientSecret, clientDetails.getClientSecret())){
            //判断  密码  是否一致
            throw new UnapprovedClientAuthenticationException("clientSecret 不匹配 " + clientId);
        }
        //密码模式 模式, 组建 authentication
        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_SORTED_MAP,clientId,clientDetails.getScope(),"password");
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        OAuth2AccessToken accessToken = tokenStore.getAccessToken(oAuth2Authentication);
        if (accessToken != null) {
            token = accessToken.getValue();
        }
        return token;
    }

    /**
     * 将登录的用户信息存放到redis中
     *
     * @param token
     * @param userDetails
     */
    private void saveUserToRedis(String token, UserDetailsDto userDetails) {
        // 获取用户登录历史token
        Object redisToken = this.redisTemplateUtil.hget(BaseRedisKeys.USER_DETAILS_TOKNE , userDetails.getUserId().toString());
        if (redisToken != null && !redisToken.toString().trim().equals("")) {
            String oldToken = String.valueOf(redisToken).trim();
            this.redisTemplateUtil.hdel(BaseRedisKeys.USER_LOGIN_TOKNE, oldToken);
            this.redisTemplateUtil.hdel(BaseRedisKeys.USER_AUTHORITIES_TOKEN, oldToken);
        }
        TokenLocalContext.remove();
        TokenLocalContext.setToken(token);
        this.redisTemplateUtil.hset(BaseRedisKeys.USER_DETAILS_TOKNE, userDetails.getUserId().toString(), token, SecurityConstant.ACCESS_TOKEN_VALIDITY_SECONDS.longValue());
        this.redisTemplateUtil.hset(BaseRedisKeys.USER_LOGIN_TOKNE, token, JSONObject.toJSONString(userDetails), SecurityConstant.ACCESS_TOKEN_VALIDITY_SECONDS.longValue());
        this.redisTemplateUtil.hset(BaseRedisKeys.USER_AUTHORITIES_TOKEN, token, userDetails.getAuthorities(), SecurityConstant.ACCESS_TOKEN_VALIDITY_SECONDS.longValue() );
    }
}
