package pers.liujunyi.cloud.security.controller.authorization;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.security.service.authorization.RoleUserMongoService;
import pers.liujunyi.cloud.security.service.authorization.RoleUserService;

/***
 * 文件名称: RoleUserController.java
 * 文件描述: 人员角色 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月22日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "人员角色 API")
@RestController
public class RoleUserController extends BaseController {

    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private RoleUserMongoService roleUserMongoService;
}
