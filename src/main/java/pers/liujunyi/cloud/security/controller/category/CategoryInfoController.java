package pers.liujunyi.cloud.security.controller.category;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;import pers.liujunyi.cloud.common.annotation.ApiVersion;
import pers.liujunyi.cloud.common.controller.BaseController;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.security.domain.IdParamDto;
import pers.liujunyi.cloud.security.domain.category.CategoryInfoDto;
import pers.liujunyi.cloud.security.domain.category.CategoryInfoQueryDto;
import pers.liujunyi.cloud.security.service.category.CategoryInfoElasticsearchService;
import pers.liujunyi.cloud.security.service.category.CategoryInfoService;

import javax.validation.Valid;

/***
 * 文件名称: CategoryInfoController.java
 * 文件描述: 分类信息 Controller
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月22日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Api(tags = "分类信息 API")
@RestController
public class CategoryInfoController extends BaseController {

    @Autowired
    private CategoryInfoService categoryInfoService;
    @Autowired
    private CategoryInfoElasticsearchService categoryInfoElasticsearchService;

    /**
     * 保存数据
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "保存数据", notes = "适用于保存数据 请求示例：127.0.0.1:18080/api/v1/verify/category/info/s")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1")
    })
    @PostMapping(value = "verify/category/info/s")
    @ApiVersion(1)
    public ResultInfo saveRecord(@Valid CategoryInfoDto param) {
        return this.categoryInfoService.saveRecord(param);
    }


    /**
     * 批量删除
     *
     * @param param 　 多个id 用 , 隔开
     * @return
     */
    @ApiOperation(value = "删除多条数据", notes = "适用于批量删除数据 请求示例：127.0.0.1:18080/api/v1/verify/category/info/d/b")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String")
    })
    @DeleteMapping(value = "verify/category/info/d/b")
    @ApiVersion(1)
    public ResultInfo batchDelete(@Valid IdParamDto param) {
        return this.categoryInfoService.deleteBatch(param.getIdList());
    }

    /**
     * 分页列表数据
     *
     * @param query
     * @return
     */
    @ApiOperation(value = "分页列表数据", notes = "适用于分页grid 显示数据 请求示例：127.0.0.1:18080/api/v1/table/category/info/g")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1")
    })
    @GetMapping(value = "table/category/info/g")
    @ApiVersion(1)
    public ResultInfo findPageGrid(CategoryInfoQueryDto query) {
        return this.categoryInfoElasticsearchService.findPageGird(query);
    }


    /**
     *  修改数据状态
     *
     * @param param
     * @return
     */
    @ApiOperation(value = "修改数据状态", notes = "适用于修改数据状态 请求示例：127.0.0.1:18080/api/v1/verify/category/info/p")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "ids", value = "ids",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "status",  required = true, dataType = "integer")
    })
    @PutMapping(value = "verify/category/info/p")
    @ApiVersion(1)
    public ResultInfo updateDataStatus(@Valid IdParamDto param ) {
        return this.categoryInfoService.updateStatus(param.getStatus(), param.getIdList());
    }


    /**
     * 根据id 获取详细信息
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id 获取详细信息", notes = "适用于根据id 获取详细信息 请求示例：127.0.0.1:18080/api/v1/table/category/info/{id}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "id", value = "id", paramType = "path",   required = true, dataType = "Long")
    })
    @GetMapping(value = "table/category/info/{id}")
    @ApiVersion(1)
    public ResultInfo findById(@PathVariable(name = "id") Long id) {
        return ResultUtil.success(this.categoryInfoElasticsearchService.findById(id));
    }

    /**
     * 分类下拉框数据
     * @param query
     * @return
     */
    @ApiOperation(value = "分类下拉框数据", notes = "分类下拉框数据 请求示例：127.0.0.1:18080/api/v1/table/category/info/select")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @GetMapping(value = "table/category/info/select")
    @ApiVersion(1)
    public ResultInfo CategoryInfoSelect(CategoryInfoQueryDto query) {
        return ResultUtil.success(this.categoryInfoElasticsearchService.categorySelect(query));
    }

    /**
     *  同步数据到es中
     * @param
     * @return
     */
    @ApiOperation(value = "同步数据", notes = "同步数据 请求示例：127.0.0.1:18080/api/v1/verify/category/info/sync")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
    })
    @PostMapping(value = "verify/category/info/sync")
    @ApiVersion(1)
    public ResultInfo syncDataToElasticsearch() {
        return this.categoryInfoService.syncDataToElasticsearch();
    }

    /**
     * 验证 名称 是否存在
     * @param categoryType  类型 10:流程分类
     * @param categoryName  最新值
     * @param history  历史值
     * @return
     */
    @ApiOperation(value = "验证 名称 是否存在", notes = "适用于验证 名称 是否存在 请求示例：127.0.0.1:18080/api/v1/table/category/info/verify/name")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "版本号", paramType = "query", required = true, dataType = "integer", defaultValue = "v1"),
            @ApiImplicitParam(name = "categoryName", value = "名称",  required = true, dataType = "String"),
            @ApiImplicitParam(name = "history", value = "历史名称",  required = false, dataType = "String"),
            @ApiImplicitParam(name = "categoryType", value = "类型",  required = true, dataType = "Int")

    })
    @GetMapping(value = "table/category/info/verify/name")
    @ApiVersion(1)
    public String  verifyCategoryName(Byte categoryType, String categoryName,  String history) {
        return this.categoryInfoElasticsearchService.verifyCategoryName(categoryType, categoryName, history);
    }
}
