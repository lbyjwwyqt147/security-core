package pers.liujunyi.cloud.security.controller.organizations;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.encrypt.annotation.Decrypt;
import pers.liujunyi.cloud.common.encrypt.annotation.Encrypt;
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
    @ApiOperation(value = "保存数据", notes = "适用于保存数据 请求示例：127.0.0.1:18080/api/v1/organization/s")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @Decrypt
    @Encrypt
    @PostMapping(value = "verify/organization/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid @RequestBody OrganizationsDto param) {
        return this.organizationsService.saveRecord(param);
    }

    /**
     * 单条删除数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "单条删除数据", notes = "适用于单条删除数据 请求示例：127.0.0.1:18080/api/v1/organization/d")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "String")
    })
    @Encrypt
    @Decrypt
    @DeleteMapping(value = "verify/organization/d")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid @RequestBody IdParamDto param) {
        this.organizationsService.deleteSingle(Long.valueOf(param.getId()));
        return ResultUtil.success();
    }

    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据", notes = "适用于批量删除数据 请求示例：127.0.0.1:18080/api/v1/organization/b/d")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String")
    })
    @Encrypt
    @Decrypt
    @DeleteMapping(value = "verify/organization/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid @RequestBody IdParamDto param) {
        return this.organizationsService.deleteBatch(param.getIdList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据", notes = "适用于分页grid 显示数据 请求示例：127.0.0.1:18080/api/v1/table/organization/g")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/organization/g")
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
    @ApiOperation(value = "根据 pid 获取 组织机构tree 结构数据 (只包含正常数据 不包含禁用数据)", notes = "适用于tree 显示数据 请求示例：127.0.0.1:18080/api/v1/tree/organization/z")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "pid", value = "pid",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/organization/z")
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
    @ApiOperation(value = "根据 pid 获取 组织机构tree 结构数据 (包含禁用数据)", notes = "适用于tree 显示数据 请求示例：127.0.0.1:18080/api/v1/tree/organization/all/z")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "pid", value = "pid",  required = true, dataType = "Long")
    })
    @GetMapping(value = "tree/organization/all/z")
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
    @ApiOperation(value = "根据 fullParentCode 获取 组织机构tree 结构数据", notes = "适用于tree 显示数据 请求示例：127.0.0.1:18080/api/v1/tree/organization/p/z")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "code", value = "code",  required = true, dataType = "String")
    })
    @GetMapping(value = "tree/organization/p/z")
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
    @ApiOperation(value = "修改数据状态", notes = "适用于修改数据状态 请求示例：127.0.0.1:18080/api/v1/organization/b/p")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @Encrypt
    @Decrypt
    @PutMapping(value = "verify/organization/p/b")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid @RequestBody IdParamDto param ) {
        return this.organizationsService.updateStatus(param.getStatus(), param.getIdList(), param.getPutParams());
    }

    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "修改数据状态", notes = "适用于修改数据状态 请求示例：127.0.0.1:18080/api/v1/organization/p")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @Encrypt
    @Decrypt
    @PutMapping(value = "verify/organization/p")
    @ApiVersion(1)
    public ResultInfo updateStatus(@Valid @RequestBody IdParamDto param ) {
        return this.organizationsService.updateStatus(param.getStatus(), param.getId(), param.getDataVersion());
    }

    /**
     * 根据id 获取详细信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id 获取详细信息", notes = "适用于根据id 获取详细信息 请求示例：127.0.0.1:18080/api/v1/organization/d/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "path",   required = true, dataType = "Long")
    })
    @Encrypt
    @GetMapping(value = "table/organization/g/{id}")
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
    @PostMapping(value = "verify/organization/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToElasticsearch() {
        return this.organizationsService.syncDataToElasticsearch();
    }
}
