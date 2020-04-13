package pers.liujunyi.cloud.security.controller.authorization;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.annotation.ControllerMethodLog;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.dto.IdParamDto;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.util.OperateLogType;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.authorization.MenuResourceDto;
import pers.liujunyi.cloud.security.domain.authorization.MenuResourceQueryDto;
import pers.liujunyi.cloud.security.entity.authorization.MenuResource;
import pers.liujunyi.cloud.security.service.authorization.MenuResourceMongoService;
import pers.liujunyi.cloud.security.service.authorization.MenuResourceService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import javax.validation.Valid;
import java.util.List;


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

    /**
     * 保存数据
     *
     * @param param
     * @return
     */
    @ControllerMethodLog(desc = "保存数据", operModule = "资源信息", serviceClass = MenuResourceService.class, entityBeanClass = MenuResource.class)
    @ApiOperation(value = "保存数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "verify/menu/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid  MenuResourceDto param) {
        return this.menuResourceService.saveRecord(param);
    }
    

    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ControllerMethodLog(desc = "删除数据", operModule = "资源信息", operType = OperateLogType.DELETE, serviceClass = MenuResourceService.class, entityBeanClass = MenuResource.class)
    @ApiOperation(value = "删除多条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String")
    })
    @DeleteMapping(value = "verify/menu/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid  IdParamDto param) {
        return this.menuResourceService.deleteBatch(param.getIdList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/menu/g")
    @ApiVersion(1)
    public ResultInfo findPageGrid(@Valid MenuResourceQueryDto query) {
        return this.menuResourceMongoService.findPageGird(query);
    }


    /**
     * 根据 pid 获取 资源tree 结构数据 (只包含正常数据 不包含禁用数据)
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据 pid 获取 资源tree 结构数据 (只包含正常数据 不包含禁用数据)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "pid", value = "pid",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/menu/z")
    @ApiVersion(1)
    public List<ZtreeNode> orgZTree(Long id) {
        return this.menuResourceMongoService.menuResourceTree(id, SecurityConstant.ENABLE_STATUS);
    }

    /**
     * 根据 pid 获取 资源tree 结构数据 (包含禁用数据)
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据 pid 获取 资源tree 结构数据 (包含禁用数据)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "pid", value = "pid",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/menu/all/z")
    @ApiVersion(1)
    public List<ZtreeNode> orgAllZTree(Long id) {
        return this.menuResourceMongoService.menuResourceTree(id, null);
    }

    /**
     * 根据 fullParentCode 获取 资源tree 结构数据
     *
     * @param code
     * @return
     */
    @ApiOperation(value = "根据 fullParentCode 获取 资源tree 结构数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "code", value = "code",  required = true, dataType = "String")
    })
    @GetMapping(value = "tree/menu/p/z")
    @ApiVersion(1)
    public List<ZtreeNode> orgParentCodeZTree(String code) {
        return this.menuResourceMongoService.menuResourceFullParentCodeTree(code);
    }


    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @ControllerMethodLog(desc = "修改数据状态", operModule = "资源信息", operType = OperateLogType.UPDATE, paramIsArray = true,  serviceClass = MenuResourceService.class, entityBeanClass = MenuResource.class, findDataMethod = "findByIdIn")
    @ApiOperation(value = "修改数据状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @PutMapping(value = "verify/menu/p/b")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.menuResourceService.updateStatus(param.getStatus(), param.getIdList());
    }


    /**
     * 根据id 获取详细信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id 获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "path",   required = true, dataType = "Long")
    })
    @GetMapping(value = "table/menu/g/{id}")
    @ApiVersion(1)
    public ResultInfo findById(@PathVariable(name = "id") Long id) {
        return this.menuResourceMongoService.selectById(id);
    }

    /**
     *  同步数据到MongoDb中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path",  dataType = "String", defaultValue = "v1"),
    })
    @PostMapping(value = "verify/menu/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToMongo() {
        return this.menuResourceService.syncDataToMongo();
    }
}
