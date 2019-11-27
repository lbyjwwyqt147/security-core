package pers.liujunyi.cloud.security.controller.authorization;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.security.service.authorization.MenuResourceMongoService;
import pers.liujunyi.cloud.security.service.authorization.MenuResourceService;


/***
 * 文件名称: MenuResourceController.java
 * 文件描述: 菜单资源 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月22日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "菜单资源 API")
@RestController
public class MenuResourceController extends BaseController {

    @Autowired
    private MenuResourceService menuResourceService;
    @Autowired
    private MenuResourceMongoService menuResourceMongoService;

}
