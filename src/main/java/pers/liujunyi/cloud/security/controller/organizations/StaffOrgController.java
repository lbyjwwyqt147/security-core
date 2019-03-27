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
import pers.liujunyi.cloud.common.util.SystemUtils;
import pers.liujunyi.cloud.security.domain.IdParamDto;
import pers.liujunyi.cloud.security.domain.organizations.StaffOrgQueryDto;
import pers.liujunyi.cloud.security.service.organizations.StaffOrgElasticsearchService;
import pers.liujunyi.cloud.security.service.organizations.StaffOrgService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/***
 * 文件名称: StaffOrgController.java
 * 文件描述: 组织机构关联人员 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月27日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "组织机构关联人员 API")
@RestController
public class StaffOrgController extends BaseController {

    @Autowired
    private StaffOrgService staffOrgService;
    @Autowired
    private StaffOrgElasticsearchService staffOrgElasticsearchService;

    /**
     * 保存数据
     *
     * @param orgId
     * @param staffIds
     * @return
     */
    @ApiOperation(value = "保存数据", notes = "适用于保存数据 请求示例：127.0.0.1:18080/api/v1/staffOrg/s")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "orgId", value = "机构id",  required = true, dataType = "Long"),
            @ApiImplicitParam(name = "staffIds", value = "人员id 多个用,隔开",  required = true, dataType = "String")

    })
    @Decrypt
    @Encrypt
    @PostMapping(value = "staffOrg/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid @NotNull(message = "组织机构 必须选择")
                                     @RequestParam(name = "orgId", required = true) Long orgId, @NotNull(message = "人员 必须选择")
    @RequestParam(name = "staffIds", required = true) String staffIds) {
        return this.staffOrgService.saveRecord(orgId, SystemUtils.idToLong(staffIds));
    }

    /**
     * 单条删除数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "单条删除数据", notes = "适用于单条删除数据 请求示例：127.0.0.1:18080/api/v1/staffOrg/d")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "String")
    })
    @Encrypt
    @Decrypt
    @DeleteMapping(value = "staffOrg/d")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid @NotNull(message = "id 必须填写")
                                       @RequestParam(name = "id", required = true) String id) {
        this.staffOrgService.singleDelete(Long.valueOf(id));
        return ResultUtil.success();
    }

    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据", notes = "适用于批量删除数据 请求示例：127.0.0.1:18080/api/v1/staffOrg/b/d")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String")
    })
    @Encrypt
    @Decrypt
    @DeleteMapping(value = "staffOrg/b/d")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid @RequestBody IdParamDto param) {
        return this.staffOrgService.batchDeletes(param.getIdList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据", notes = "适用于分页grid 显示数据 请求示例：127.0.0.1:18080/api/v1/table/staffOrg/g")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1")
    })
    @Encrypt
    @GetMapping(value = "table/staffOrg/g")
    @ApiVersion(1)
    public ResultInfo findPageGrid(@Valid StaffOrgQueryDto query) {
        return this.staffOrgElasticsearchService.findPageGird(query);
    }


    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "修改数据状态", notes = "适用于修改数据状态 请求示例：127.0.0.1:18080/api/v1/staffOrg/p")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @Encrypt
    @Decrypt
    @PutMapping(value = "staffOrg/p")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid @RequestBody IdParamDto param ) {
        return this.staffOrgService.updateStatus(param.getStatus(), param.getIdList());
    }


    /**
     *  同步数据到es中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据", notes = "同步数据 请求示例：127.0.0.1:18080/api/v1/staffOrg/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "path", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PostMapping(value = "staffOrg/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToElasticsearch() {
        return this.staffOrgService.syncDataToElasticsearch();
    }
}
