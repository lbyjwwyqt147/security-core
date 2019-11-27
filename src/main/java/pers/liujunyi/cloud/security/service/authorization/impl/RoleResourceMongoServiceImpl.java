package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseMongoServiceImpl;
import pers.liujunyi.cloud.security.entity.authorization.RoleResource;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleResourceMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.RoleResourceMongoService;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: RoleResourceMongoServiceImpl.java
 * 文件描述: 角色资源 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class RoleResourceMongoServiceImpl extends BaseMongoServiceImpl<RoleResource, Long> implements RoleResourceMongoService {

    @Autowired
    private RoleResourceMongoRepository roleResourceMongoRepository;

    public RoleResourceMongoServiceImpl(BaseMongoRepository<RoleResource, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }


    @Override
    public List<RoleResource> findByResourceIdAndStatus(Long resourceId, Byte status) {
        return null;
    }

    @Override
    public List<RoleResource> findByResourceIdIn(List<Long> resourceIds) {
        return null;
    }

    @Override
    public List<RoleResource> findByResourceIdInOrderByIdAsc(List<Long> resourceIds) {
        return null;
    }

    @Override
    public ResultInfo findPageGird(RoleResource query) {
        return null;
    }

    @Override
    public List<RoleResource> findByRoleIdAndStatus(Long roleId, Byte status) {
        return null;
    }

    @Override
    public List<RoleResource> findByRoleIdIn(List<Long> roleIds) {
        return null;
    }

    @Override
    public List<RoleResource> getResourceInfoByRoleId(Long roleId) {
        return null;
    }

    @Override
    public Map<Long, List<RoleResource>> getResourceInfoByRoleIdIn(List<Long> roleId) {
        return null;
    }

    @Override
    public List<RoleResource> getResourceInfoByResourceId(Long resourceId) {
        return null;
    }

    @Override
    public Map<Long, List<RoleResource>> getResourceInfoByResourceIdIn(List<Long> resourceIds) {
        return null;
    }
}
