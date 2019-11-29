package pers.liujunyi.cloud.security.controller.organizations;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.dto.IdParamDto;
import pers.liujunyi.cloud.common.encrypt.annotation.Decrypt;
import pers.liujunyi.cloud.common.encrypt.annotation.Encrypt;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.util.SystemUtils;
import pers.liujunyi.cloud.security.domain.organizations.StaffOrgQueryDto;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;
import pers.liujunyi.cloud.security.service.organizations.StaffOrgMongoService;
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
    private StaffOrgMongoService staffOrgElasticsearchService;

    /**
     * 保存数据
     *
     * @param org
     * @param staffIds
     * @return
     */
    @ApiOperation(value = "保存数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "org", value = "机构 接收参数格式 json 字符串   {orgId=机构id,orgNumber=机构编号,fullParent=机构父id}",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "staffIds", value = "人员id 多个用,隔开",  required = true, dataType = "String")

    })
    @Decrypt
    @Encrypt
    @PostMapping(value = "verify/staffOrg/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid @NotNull(message = "组织机构 必须选择")
                                     @RequestParam(name = "org", required = true) String org, @NotNull(message = "人员 必须选择")
    @RequestParam(name = "staffIds", required = true) String staffIds) {
        return this.staffOrgService.saveRecord(JSON.parseObject(org, StaffOrg.class), SystemUtils.idToLong(staffIds));
    }

    /**
     * 单条删除数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "单条删除数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "String")
    })
    @Encrypt
    @Decrypt
    @DeleteMapping(value = "verify/staffOrg/d")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid @NotNull(message = "id 必须填写")
                                       @RequestParam(name = "id", required = true) String id) {
        this.staffOrgService.deleteSingle(Long.valueOf(id));
        return ResultUtil.success();
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
    @Encrypt
    @Decrypt
    @DeleteMapping(value = "verify/staffOrg/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid @RequestBody IdParamDto param) {
        return this.staffOrgService.deleteBatch(param.getIdList());
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
    @ApiOperation(value = "修改数据状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @Encrypt
    @Decrypt
    @PutMapping(value = "verify/staffOrg/p")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid @RequestBody IdParamDto param ) {
        return this.staffOrgService.updateStatus(param.getStatus(), param.getIdList());
    }


    /**
     *  同步数据到es中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PostMapping(value = "verify/staffOrg/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToMongo() {
        return this.staffOrgService.syncDataToMongo();
    }
}
