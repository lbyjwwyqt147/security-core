package pers.liujunyi.cloud.security.controller.user;

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
import pers.liujunyi.cloud.security.domain.user.UserAccountsDto;
import pers.liujunyi.cloud.security.domain.user.UserAccountsQueryDto;
import pers.liujunyi.cloud.security.service.user.UserAccountsMongoService;
import pers.liujunyi.cloud.security.service.user.UserAccountsService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/***
 * 文件名称: UserAccountsController.java
 * 文件描述: 用户账户 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月21日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "用户账户 API")
@RestController
public class UserAccountsController extends BaseController {

    @Autowired
    private UserAccountsService userAccountsService;
    @Autowired
    private UserAccountsMongoService userAccountsMongoService;

    /**
     * 注册账户
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "注册账户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1")
    })
    @Encrypt
    @Decrypt
    @PostMapping(value = "verify/ignore/accounts/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid @RequestBody UserAccountsDto param) {
        return this.userAccountsService.saveRecord(param);
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
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "Long")
    })
    @Encrypt
    @Decrypt
    @DeleteMapping(value = "verify/accounts/d")
    @ApiVersion(1)
    public ResultInfo singleDelete(@Valid @NotNull(message = "id 必须填写")
                                       @RequestParam(name = "id", required = true) String id) {
        this.userAccountsService.deleteSingle(Long.valueOf(id));
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
    @Decrypt
    @Encrypt
    @DeleteMapping(value = "verify/accounts/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid @RequestBody IdParamDto param) {
        return this.userAccountsService.deleteBatch(param.getIdList());
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
    @PutMapping(value = "verify/accounts/p/b")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid @RequestBody IdParamDto param ) {
        return this.userAccountsService.updateStatus(param.getStatus(), param.getIdList(), param.getPutParams());
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
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @Encrypt
    @Decrypt
    @PutMapping(value = "verify/accounts/p")
    @ApiVersion(1)
    public ResultInfo updateStatus(@Valid @RequestBody IdParamDto param ) {
        return this.userAccountsService.updateStatus(param.getStatus(), param.getId(), param.getDataVersion());
    }

    /**
     *  重置密码
     *
     * @param id
     * @param historyPassWord
     * @param currentPassWord
     * @return
     */
    @ApiOperation(value = "修改重置密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id",  required = true, dataType = "integer"),
            @ApiImplicitParam(name = "historyPassWord", value = "原始密码",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "currentPassWord", value = "新密码",  required = true, dataType = "String")
    })
    @Encrypt
    @Decrypt
    @PutMapping(value = "verify/accounts/p/r")
    @ApiVersion(1)
    public ResultInfo resetPassword(@Valid @NotNull(message = "id 必须填写")
                                        @RequestParam(name = "id", required = true) Long id, @NotBlank(message = "原始密码 必须填写")
    @RequestParam(name = "historyPassWord", required = true) String historyPassWord, @NotBlank(message = "新密码 必须填写")
    @RequestParam(name = "currentPassWord", required = true) String currentPassWord, @NotBlank(message = "版本号 必须填写")
    @RequestParam(name = "dataVersion", required = true) Long dataVersion) {
        return this.userAccountsService.updateUserPassWord(id, historyPassWord, currentPassWord, dataVersion);
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
    @GetMapping(value = "table/accounts/g")
    @ApiVersion(1)
    public ResultInfo encryptPageGrid(@Valid UserAccountsQueryDto query) {
        return this.userAccountsMongoService.findPageGird(query);
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
    @PostMapping(value = "verify/accounts/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToMongo() {
        return this.userAccountsService.syncDataToMongo();
    }
}
