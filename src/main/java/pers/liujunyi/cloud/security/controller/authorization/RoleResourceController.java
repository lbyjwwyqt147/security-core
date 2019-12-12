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
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.entity.authorization.RoleResource;
import pers.liujunyi.cloud.security.service.authorization.RoleResourceMongoService;
import pers.liujunyi.cloud.security.service.authorization.RoleResourceService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/***
 * 文件名称: RoleResourceController.java
 * 文件描述: 角色资源 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月22日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "角色资源 API")
@RestController
public class RoleResourceController extends BaseController {

    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private RoleResourceMongoService roleResourceMongoService;
    
    /**
     * 保存数据
     *
     * @param resource
     * @param resourceIds
     * @return
             */
    @ApiOperation(value = "保存数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "resource", value = "resource",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "resourceIds", value = "资源id 多个用,隔开",  required = true, dataType = "String")

    })
    @PostMapping(value = "verify/role/resource/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(RoleResource resource, @Valid @NotNull(message = "资源 必须选择")
                                 @RequestParam(name = "resourceIds", required = true) String resourceIds) {
        return this.roleResourceService.saveRecord(resource, SystemUtils.idToLong(resourceIds));
    }

    /**
     * 根据 角色ID 资源PID 获取 资源tree 结构数据
     *
     * @param roleId  角色ID
     * @param resourcePid         资源ID
     * @return
     */
    @ApiOperation(value = "根据 角色ID 资源PID 获取 资源tree 结构数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "roleId", value = "roleId",  required = true, dataType = "Long"),
            @ApiImplicitParam(name = "resourcePid", value = "resourcePid",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/role/resource/z")
    @ApiVersion(1)
    public List<ZtreeNode> resourceSelectedTree(Long roleId, Long resourcePid) {
        return this.roleResourceMongoService.resourceSelectedTree(roleId, resourcePid);
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
    @DeleteMapping(value = "verify/role/resource/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid IdParamDto param) {
        return this.roleResourceService.deleteBatch(param.getIdList());
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
    @GetMapping(value = "table/role/resource/g")
    @ApiVersion(1)
    public ResultInfo findPageGrid(RoleResource query) {
        return this.roleResourceMongoService.findPageGird(query);
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
    @PutMapping(value = "verify/role/resource/p")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid  IdParamDto param ) {
        return this.roleResourceService.updateStatus(param.getStatus(), param.getIdList());
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
    @PostMapping(value = "verify/role/resource/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToMongo() {
        return this.roleResourceService.syncDataToMongo();
    }
}
