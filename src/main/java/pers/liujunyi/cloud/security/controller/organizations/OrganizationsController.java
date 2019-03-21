package pers.liujunyi.cloud.security.controller.organizations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.IdParamDto;
import pers.liujunyi.cloud.security.domain.organizations.OrganizationsDto;
import pers.liujunyi.cloud.security.domain.organizations.OrganizationsQueryDto;
import pers.liujunyi.cloud.security.service.organizations.OrganizationsElasticsearchService;
import pers.liujunyi.cloud.security.service.organizations.OrganizationsService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/***
 * 文件名称: OrganizationsController.java
 * 文件描述: 组织机构 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年01月17日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "组织机构 API")
@RestController
public class OrganizationsController extends BaseController {

    @Autowired
    private OrganizationsService organizationsService;
    @Autowired
    private OrganizationsElasticsearchService organizationsElasticsearchService;

    /**
     * 保存数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "保存数据", notes = "适用于保存数据 请求示例：127.0.0.1:18080/api/v1/organization/save")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "organization/save")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid OrganizationsDto param) {
        return this.organizationsService.saveRecord(param);
    }

    /**
     * 单条删除数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "单条删除数据", notes = "适用于单条删除数据 请求示例：127.0.0.1:18080/api/v1/organization/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "Long")
    })
    @DeleteMapping(value = "organization/delete")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid @NotNull(message = "id 必须填写")
                                       @RequestParam(name = "id", required = true) Long id) {
        this.organizationsService.singleDelete(id);
        return ResultUtil.success();
    }

    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据", notes = "适用于批量删除数据 请求示例：127.0.0.1:18080/api/v1/organization/batchDelete")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String")
    })
    @DeleteMapping(value = "organization/batchDelete")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid IdParamDto param) {
        return this.organizationsService.batchDeletes(param.getIdList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据", notes = "适用于分页grid 显示数据 请求示例：127.0.0.1:18080/api/v1/organization/grid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/organization/grid")
    @ApiVersion(1)
    public ResultInfo findPageGrid(@Valid OrganizationsQueryDto query) {
        return this.organizationsElasticsearchService.findPageGird(query);
    }


    /**
     * 根据 pid 获取 组织机构tree 结构数据 (只包含正常数据 不包含禁用数据)
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据 pid 获取 组织机构tree 结构数据 (只包含正常数据 不包含禁用数据)", notes = "适用于tree 显示数据 请求示例：127.0.0.1:18080/api/v1/organization/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "pid", value = "pid",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/organization/ztree")
    @ApiVersion(1)
    public List<ZtreeNode> orgZTree(Long id) {
        return this.organizationsElasticsearchService.orgTree(id, SecurityConstant.ENABLE_STATUS);
    }

    /**
     * 根据 pid 获取 组织机构tree 结构数据 (包含禁用数据)
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据 pid 获取 组织机构tree 结构数据 (包含禁用数据)", notes = "适用于tree 显示数据 请求示例：127.0.0.1:18080/api/v1/organization/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "pid", value = "pid",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/organization/all/ztree")
    @ApiVersion(1)
    public List<ZtreeNode> orgAllZTree(Long id) {
        return this.organizationsElasticsearchService.orgTree(id, null);
    }

    /**
     * 根据 fullParentCode 获取 组织机构tree 结构数据
     *
     * @param code
     * @return
     */
    @ApiOperation(value = "根据 fullParentCode 获取 组织机构tree 结构数据", notes = "适用于tree 显示数据 请求示例：127.0.0.1:18080/api/v1/organization/tree")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "code", value = "code",  required = true, dataType = "String")
    })
    @GetMapping(value = "tree/organization/parentCode/ztree")
    @ApiVersion(1)
    public List<ZtreeNode> orgParentCodeZTree(String code) {
        return this.organizationsElasticsearchService.orgFullParentCodeTree(code);
    }


    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "修改数据状态", notes = "适用于修改数据状态 请求示例：127.0.0.1:18080/api/v1/organization/status")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @PutMapping(value = "organization/status")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.organizationsService.updateStatus(param.getStatus(), param.getIdList());
    }


    /**
     * 根据id 获取详细信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id 获取详细信息", notes = "适用于根据id 获取详细信息 请求示例：127.0.0.1:18080/api/v1/organization/details/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "path",   required = true, dataType = "Long")
    })
    @GetMapping(value = "organization/details/{id}")
    @ApiVersion(1)
    public ResultInfo findById(@PathVariable(name = "id") Long id) {
        return this.organizationsElasticsearchService.selectById(id);
    }

    /**
     *  同步数据到es中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据", notes = "同步数据 请求示例：127.0.0.1:18080/api/v1/organization/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PostMapping(value = "organization/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToElasticsearch() {
        return this.organizationsService.syncDataToElasticsearch();
    }
}
