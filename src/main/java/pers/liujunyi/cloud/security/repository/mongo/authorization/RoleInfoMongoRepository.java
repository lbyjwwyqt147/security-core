package pers.liujunyi.cloud.security.repository.mongo.authorization;

import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.security.entity.authorization.RoleInfo;

import java.util.List;

/***
 * 文件名称: RoleInfoMongoRepository.java
 * 文件描述: 角色信息 Mongo Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface RoleInfoMongoRepository extends BaseMongoRepository<RoleInfo, Long> {

    /**
     * 根据 编号 获取数据
     * @param roleNumber 编号
     * @return
     */
    RoleInfo findFirstByRoleNumber(String roleNumber);

    /**
     * 根据pid 获取数据
     * @param pid
     * @param roleStatus  0：正常  1：禁用
     * @return
     */
    List<RoleInfo> findByParentIdAndRoleStatus(Long pid, Byte roleStatus);

    /**
     * 根据 fullRoleParentCode 获取数据
     * @param fullRoleParentCode
     * @param roleStatus  0：正常  1：禁用
     * @return
     */
    List<RoleInfo> findByFullRoleParentCodeLikeAndRoleStatus(String fullRoleParentCode, Byte roleStatus);

}
