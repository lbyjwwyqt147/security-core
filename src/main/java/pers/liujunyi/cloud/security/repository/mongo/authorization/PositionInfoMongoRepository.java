package pers.liujunyi.cloud.security.repository.mongo.authorization;

import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.security.entity.authorization.PositionInfo;

import java.util.List;

/***
 * 文件名称: PositionInfoMongoRepository.java
 * 文件描述: 岗位信息 Mongo Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface PositionInfoMongoRepository extends BaseMongoRepository<PositionInfo, Long> {

    /**
     * 根据 编号 获取数据
     * @param postNumber 编号
     * @return
     */
    PositionInfo findFirstByPostNumber(String postNumber);

    /**
     * 根据pid 获取数据
     * @param pid
     * @param postStatus  0：正常  1：禁用
     * @return
     */
    List<PositionInfo> findByParentIdAndPostStatusOrderBySerialNumberAsc(Long pid, Byte postStatus);

    /**
     * 根据postStatus获取数据
     * @param postStatus  0：正常  1：禁用
     * @return
     */
    List<PositionInfo> findByPostStatusOrderBySerialNumberAsc(Byte postStatus);


    /**
     * 根据pid 获取数据
     * @param pid
     * @return
     */
    List<PositionInfo> findByParentIdOrderBySerialNumberAsc(Long pid);

    /**
     * 根据 fullPostParentCode 获取数据
     * @param fullPostParentCode
     * @param postStatus  0：正常  1：禁用
     * @return
     */
    List<PositionInfo> findByFullPostParentCodeLikeAndPostStatusOrderBySerialNumberAsc(String fullPostParentCode, Byte postStatus);

}
