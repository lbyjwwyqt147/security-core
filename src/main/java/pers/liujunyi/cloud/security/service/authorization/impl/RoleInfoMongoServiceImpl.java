package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseMongoServiceImpl;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.authorization.RoleInfoQueryDto;
import pers.liujunyi.cloud.security.entity.authorization.RoleInfo;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleInfoMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.RoleInfoMongoService;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: RoleInfoMongoServiceImpl.java
 * 文件描述: 角色 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class RoleInfoMongoServiceImpl  extends BaseMongoServiceImpl<RoleInfo, Long> implements RoleInfoMongoService {

    @Autowired
    private RoleInfoMongoRepository roleInfoMongoRepository;

    public RoleInfoMongoServiceImpl(BaseMongoRepository<RoleInfo, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }

    @Override
    public List<ZtreeNode> roleTree(Long pid, Byte status) {
        return null;
    }

    @Override
    public List<ZtreeNode> roleFullParentCodeTree(String fullParentCode) {
        return null;
    }

    @Override
    public ResultInfo findPageGird(RoleInfoQueryDto query) {
        return null;
    }

    @Override
    public String getRoleName(Long id) {
        return null;
    }

    @Override
    public Map<Long, String> findKeyIdValueNameByIdIn(List<Long> ids) {
        return null;
    }

    @Override
    public ResultInfo selectById(Long id) {
        return null;
    }

    @Override
    public Map<Long, String> roleNameToMap(List<Long> ids) {
        return null;
    }
}
