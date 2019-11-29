package pers.liujunyi.cloud.security.controller.authorization;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.dto.IdParamDto;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.util.SystemUtils;
import pers.liujunyi.cloud.security.entity.authorization.RoleUser;
import pers.liujunyi.cloud.security.service.authorization.RoleUserMongoService;
import pers.liujunyi.cloud.security.service.authorization.RoleUserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    /**
     * 保存数据
     *
     * @param user
     * @param roleIds
     * @return
     */
    @ApiOperation(value = "保存数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "user", value = "user",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "roleIds", value = "用户id 多个用,隔开",  required = true, dataType = "String")

    })
    @PostMapping(value = "verify/role/user/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(RoleUser user, @Valid @NotNull(message = "用户 必须选择")
    @RequestParam(name = "userIds", required = true) String roleIds) {
        return this.roleUserService.saveRecord(user, SystemUtils.idToLong(roleIds));
    }


    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String")
    })
    @DeleteMapping(value = "verify/role/user/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid IdParamDto param) {
        return this.roleUserService.deleteBatch(param.getIdList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/role/user/g")
    @ApiVersion(1)
    public ResultInfo findPageGrid(RoleUser query) {
        return this.roleUserMongoService.findPageGird(query);
    }


    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "修改数据状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @PutMapping(value = "verify/role/user/p")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.roleUserService.updateStatus(param.getStatus(), param.getIdList());
    }


    /**
     *  同步数据到mongo中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PostMapping(value = "verify/role/user/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToMongo() {
        return this.roleUserService.syncDataToMongo();
    }
}
