package pers.liujunyi.cloud.security.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.encrypt.annotation.Decrypt;
import pers.liujunyi.cloud.common.encrypt.annotation.Encrypt;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.security.domain.user.LoginDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

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

    /**
     * 获取当前登录用户信息
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
    @ApiOperation(value = "用户登陆", notes = "")
    @PostMapping(value = "user/login")
    @ApiVersion(1)
    @Encrypt
    @Decrypt
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
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserAccount(), loginDto.getUserPassword()));
            //将身份 存储到SecurityContext里
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            request.getSession().setAttribute("SPRING_SECURITY_CONTEXT", securityContext); // 这个非常重要，否则验证后将无法登陆
            return ResultUtil.success("登录成功.");
        } catch (AuthenticationException e){
            e.printStackTrace();
            return ResultUtil.fail("用户或者密码错误.");
        }
    }

    /**
     * 获取当前登录用户信息
     * @param user
     * @return
     */
    @ApiOperation(value = "获取当前登录用户信息", notes = "适用于获取当前登录用户信息 请求示例：127.0.0.1:18080/api/v1/verify/user/info")
    @GetMapping(value = "verify/user/info")
    public Principal user(Principal user) {
        //获取当前用户信息
        log.debug("user", user);
        return user;
    }



    /**
     * 用户登录退出
     * @param access_token
     * @return
     */
    @ApiOperation(value = "用户登录退出", notes = "适用于用户登录退出 请求示例：127.0.0.1:18080/api/v1/verify/user/exit")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "access_token", value = "access_token",  required = true, dataType = "String"),
    })
    @DeleteMapping(value = "verify/user/exit")
    @ApiVersion(1)
    public ResultInfo revokeToken(String access_token) {
        //注销当前用户
        if (consumerTokenServices.revokeToken(access_token)) {
            return ResultUtil.success();
        } else {
            return ResultUtil.fail();
        }
    }

}
