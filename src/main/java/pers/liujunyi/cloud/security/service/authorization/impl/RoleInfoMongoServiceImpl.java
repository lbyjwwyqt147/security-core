package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseMongoServiceImpl;
import pers.liujunyi.cloud.common.vo.tree.ZtreeBuilder;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.authorization.RoleInfoQueryDto;
import pers.liujunyi.cloud.security.entity.authorization.RoleInfo;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleInfoMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.RoleInfoMongoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    public RoleInfo findFirstByRoleNumber(String roleNumber) {
        return this.roleInfoMongoRepository.findFirstByRoleNumber(roleNumber);
    }

    @Override
    public List<ZtreeNode> roleTree(Long pid, Byte status) {
        List<RoleInfo> list = null;
        if (status == null) {
            list = this.roleInfoMongoRepository.findByParentId(pid);
        } else {
            list = this.roleInfoMongoRepository.findByParentIdAndRoleStatus(pid,  status);
        }
        return this.startBuilderZtree(list);
    }

    @Override
    public List<ZtreeNode> roleFullParentCodeTree(String fullParentCode) {
        List<RoleInfo> list = this.roleInfoMongoRepository.findByFullRoleParentCodeLikeAndRoleStatus(fullParentCode,  SecurityConstant.ENABLE_STATUS);
        return this.startBuilderZtree(list);
    }

    @Override
    public ResultInfo findPageGird(RoleInfoQueryDto query) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        Pageable pageable = query.toPageable(sort);
        // 查询条件
        Query searchQuery = query.toSpecPageable(pageable);
        // 查询总记录条数
        long totalElements = this.mongoDbTemplate.count(searchQuery, RoleInfo.class);
        // 查询数据
        List<RoleInfo> searchPageResults =  this.mongoDbTemplate.find(searchQuery, RoleInfo.class);
        ResultInfo result = ResultUtil.success(searchPageResults, super.secretKey);
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public String getRoleName(Long id) {
        RoleInfo roleInfo = this.findById(id);
        if (roleInfo != null) {
            return roleInfo.getRoleName();
        }
        return null;
    }

    @Override
    public ResultInfo selectById(Long id) {
        return ResultUtil.success(this.findById(id));
    }

    @Override
    public Map<Long, String> roleNameToMap(List<Long> ids) {
        List<RoleInfo> list = this.roleInfoMongoRepository.findByIdIn(ids);
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().collect(Collectors.toMap(RoleInfo::getId, RoleInfo::getRoleName));
        }
        return null;
    }

    @Override
    public RoleInfo findById(Long id) {
        Optional<RoleInfo> roleInfo = this.roleInfoMongoRepository.findById(id);
        if (roleInfo.isPresent()) {
            return roleInfo.get();
        }
        return null;
    }

    /**
     * 构建 ztree
     * @param list
     * @return
     */
    private List<ZtreeNode> startBuilderZtree( List<RoleInfo> list){
        List<ZtreeNode> treeNodes = new LinkedList<>();
        if (!CollectionUtils.isEmpty(list)){
            list.stream().forEach(item -> {
                ZtreeNode zTreeNode = new ZtreeNode(item.getId(), item.getParentId(), item.getRoleName());
                Map<String, String> attributesMap = new ConcurrentHashMap<>(2);
                attributesMap.put("fullParent", item.getFullRoleParent());
                attributesMap.put("roleNumber", item.getRoleNumber());
                zTreeNode.setOtherAttributes(attributesMap);
                treeNodes.add(zTreeNode);
            });
        }
        return ZtreeBuilder.buildListToTree(treeNodes);
    }
}
