package pers.liujunyi.cloud.security.domain;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import pers.liujunyi.cloud.common.util.SystemUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
public class IdParamDto implements Serializable {
    private static final long serialVersionUID = -6603286179991508260L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "pid")
    private Long pid;

    @ApiModelProperty(value = "otherId")
    private Long otherId;

    /** 一组 id  必须是 1,2,3 格式  */
    @ApiModelProperty(value = "ids")
    private String ids;

    /** 一组 id  必须是 1,2,3 格式  */
    @ApiModelProperty(value = "otherId")
    private String otherIds;

    @ApiModelProperty(value = "idList")
    private List<Long> idList;

    @ApiModelProperty(value = "otherIdList")
    private List<Long> otherIdList;

    @ApiModelProperty(value = "code")
    private String code;

    /** 一组 code  必须是 1,2,3 格式  */
    @ApiModelProperty(value = "codes")
    private String codes;

    @ApiModelProperty(value = "codes")
    private List<String> codeList;

    /**  修改状态时前端传的json数组   格式必须是  [{id=1,dataVersion=}] */
    @ApiModelProperty(value = "putParams")
    private String putParams;

    @ApiModelProperty(value = "状态")
    private Byte status;

    @ApiModelProperty(value = "版本号")
    private Long dataVersion;

    public void setIds(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            try {
                this.setIdList(JSONArray.parseArray(ids, Long.class));
            } catch (Exception e) {
                this.setIdList(SystemUtils.idToLong(ids));
            }
        }
        this.ids = ids;
    }

    public void setCodes(String codes) {
        if (StringUtils.isNotBlank(codes)) {
            try {
                this.setCodeList(JSONArray.parseArray(codes, String.class));
            } catch (Exception e) {
                this.setCodeList(SystemUtils.stringToList(codes));
            }
        }
        this.codes = codes;
    }

    public void setOtherIds(String otherIds) {
        if (StringUtils.isNotBlank(otherIds)) {
            try {
                this.setOtherIdList(JSONArray.parseArray(otherIds, Long.class));
            } catch (Exception e) {
                this.setOtherIdList(SystemUtils.idToLong(otherIds));
            }
        }
        this.otherIds = otherIds;
    }
}
