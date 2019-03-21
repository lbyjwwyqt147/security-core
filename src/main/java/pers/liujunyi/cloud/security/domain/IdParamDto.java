package pers.liujunyi.cloud.security.domain;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

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


    @ApiModelProperty(value = "状态")
    private Byte status;

    public void setIds(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            this.setIdList(JSONArray.parseArray(ids, Long.class));
        }
        this.ids = ids;
    }

    public void setCodes(String codes) {
        if (StringUtils.isNotBlank(codes)) {
            this.setCodeList(JSONArray.parseArray(codes, String.class));
        }
        this.codes = codes;
    }

    public void setOtherIds(String otherIds) {
        if (StringUtils.isNotBlank(otherIds)) {
            this.setOtherIdList(JSONArray.parseArray(otherIds, Long.class));
        }
        this.otherIds = otherIds;
    }
}
