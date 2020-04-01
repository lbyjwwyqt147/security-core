package pers.liujunyi.cloud.security.controller.authorization;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.annotation.MethodLog;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.dto.IdParamDto;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.util.OperateLogType;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.authorization.PositionInfoDto;
import pers.liujunyi.cloud.security.domain.authorization.PositionInfoQueryDto;
import pers.liujunyi.cloud.security.service.authorization.PositionInfoMongoService;
import pers.liujunyi.cloud.security.service.authorization.PositionInfoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import javax.validation.Valid;
import java.util.List;

/***
 * 文件名称: PositionInfoController.java
 * 文件描述: 岗位 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月22日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "岗位信息 API")
@RestController
public class PositionInfoController extends BaseController {

    @Autowired
    private PositionInfoService positionInfoService;
    @Autowired
    private PositionInfoMongoService positionInfoMongoService;

    /**
     * 保存数据
     *
     * @param param
     * @return
     */
    @MethodLog(desc = "保存数据", operModule = "岗位信息", serviceClass = "PositionInfoService")
    @ApiOperation(value = "保存数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "verify/position/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid  PositionInfoDto param) {
        return this.positionInfoService.saveRecord(param);
    }


    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @MethodLog(desc = "删除数据", operModule = "岗位信息", operType = OperateLogType.DELETE)
    @ApiOperation(value = "删除多条数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String")
    })
    @DeleteMapping(value = "verify/position/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid  IdParamDto param) {
        return this.positionInfoService.deleteBatch(param.getIdList());
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
    @GetMapping(value = "table/position/g")
    @ApiVersion(1)
    public ResultInfo findPageGrid(@Valid PositionInfoQueryDto query) {
        return this.positionInfoMongoService.findPageGird(query);
    }


    /**
     * 根据 pid 获取 岗位tree 结构数据 (只包含正常数据 不包含禁用数据)
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据 pid 获取 岗位tree 结构数据 (只包含正常数据 不包含禁用数据)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "pid", value = "pid",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/position/z")
    @ApiVersion(1)
    public List<ZtreeNode> orgZTree(Long id) {
        return this.positionInfoMongoService.positionTree(id, SecurityConstant.ENABLE_STATUS);
    }

    /**
     * 根据 pid 获取 岗位tree 结构数据 (包含禁用数据)
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据 pid 获取 岗位tree 结构数据 (包含禁用数据)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "pid", value = "pid",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/position/all/z")
    @ApiVersion(1)
    public List<ZtreeNode> orgAllZTree(Long id) {
        return this.positionInfoMongoService.positionTree(id, null);
    }

    /**
     * 根据 fullParentCode 获取 岗位tree 结构数据
     *
     * @param code
     * @return
     */
    @ApiOperation(value = "根据 fullParentCode 获取 岗位tree 结构数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "code", value = "code",  required = true, dataType = "String")
    })
    @GetMapping(value = "tree/position/p/z")
    @ApiVersion(1)
    public List<ZtreeNode> orgParentCodeZTree(String code) {
        return this.positionInfoMongoService.positionFullParentCodeTree(code);
    }


    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @MethodLog(desc = "修改数据状态", operModule = "岗位信息", operType = OperateLogType.UPDATE, paramIsArray = true, serviceClass = "PositionInfoService", findDataMethod = "findByIdIn")
    @ApiOperation(value = "修改数据状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @PutMapping(value = "verify/position/p/b")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.positionInfoService.updateStatus(param.getStatus(), param.getIdList());
    }


    /**
     * 根据id 获取详细信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id 获取详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "path",   required = true, dataType = "Long")
    })
    @GetMapping(value = "table/position/g/{id}")
    @ApiVersion(1)
    public ResultInfo findById(@PathVariable(name = "id") Long id) {
        return this.positionInfoMongoService.selectById(id);
    }

    /**
     *  同步数据到MongoDb中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PostMapping(value = "verify/position/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToMongo() {
        return this.positionInfoService.syncDataToMongo();
    }

}
