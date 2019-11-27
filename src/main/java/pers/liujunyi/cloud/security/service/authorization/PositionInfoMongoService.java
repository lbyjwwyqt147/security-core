package pers.liujunyi.cloud.security.service.authorization;

import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.BaseMongoService;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.authorization.PositionInfoQueryDto;
import pers.liujunyi.cloud.security.entity.authorization.PositionInfo;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: PositionInfoMongoService.java
 * 文件描述: 岗位信息 Mongo Service
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface PositionInfoMongoService extends BaseMongoService<PositionInfo, Long> {

    /**
     *  根据 pid 符合 ztree 结构的数据
     * @param pid
     * @param status
     * @return
     */
    List<ZtreeNode> positionTree(Long pid, Byte status);

    /**
     * 根据 fullPostParentCode 获取 符合 ztree 结构的数据
     * @param fullParentCode
     * @return
     */
    List<ZtreeNode> positionFullParentCodeTree(String fullParentCode);

    /**
     * 分页列表
     * @param query
     * @return
     */
    ResultInfo findPageGird(PositionInfoQueryDto query);

    /**
     * 根据ID 获取岗位名称
     * @param id
     * @return
     */
    String getPositionName(Long id);

    /**
     * 根据一组id 获取数据
     * @param ids
     * @return key = id  value = name
     */
    Map<Long, String> findKeyIdValueNameByIdIn(List<Long> ids);

    /**
     * 根据ID获取详细信息
     * @param id
     * @return
     */
    ResultInfo selectById(Long id);


    /**
     * 根据 ID  获取 岗位名称 返回 map
     * @param ids
     * @return 返回  map  key = id  valud  = 岗位名称
     */
    Map<Long, String> positionNameToMap(List<Long> ids);
    
}
