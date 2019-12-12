package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseMongoServiceImpl;
import pers.liujunyi.cloud.common.vo.tree.ZtreeBuilder;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.entity.authorization.MenuResource;
import pers.liujunyi.cloud.security.entity.authorization.RoleResource;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleResourceMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.MenuResourceMongoService;
import pers.liujunyi.cloud.security.service.authorization.RoleResourceMongoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
    @Autowired
    private MenuResourceMongoService menuResourceMongoService;

    public RoleResourceMongoServiceImpl(BaseMongoRepository<RoleResource, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }


    @Override
    public List<RoleResource> findByResourceIdAndStatus(Long resourceId, Byte status) {
        return this.roleResourceMongoRepository.findByResourceIdAndStatus(resourceId, status);
    }

    @Override
    public List<RoleResource> findByResourceIdInAndStatus(List<Long> resourceIds, Byte status) {
        return this.roleResourceMongoRepository.findByResourceIdInAndStatus(resourceIds, status);
    }

    @Override
    public List<ZtreeNode> resourceSelectedTree(Long roleId, Long resourcePid) {
        // 获取资源数据
        List<MenuResource> resourceList = this.menuResourceMongoService.findList(resourcePid, SecurityConstant.ENABLE_STATUS);
        // 角色匹配的资源数据
        List<RoleResource> roleResources = this.findByRoleIdAndStatus(roleId, SecurityConstant.ENABLE_STATUS);
        Map<Long, RoleResource> roleResourceMap = roleResources.stream().collect(Collectors.toMap(RoleResource::getResourceId, roleResource -> roleResource));
        return startBuilderZtree(resourceList, roleResourceMap);
    }


    @Override
    public ResultInfo findPageGird(RoleResource query) {

        return null;
    }

    @Override
    public List<RoleResource> findByRoleIdAndStatus(Long roleId, Byte status) {
        return this.roleResourceMongoRepository.findByRoleIdAndStatus(roleId, status);
    }

    @Override
    public List<RoleResource> findByRoleIdInAndStatus(List<Long> roleIds, Byte status) {
        return this.roleResourceMongoRepository.findByRoleIdInAndStatus(roleIds, status);
    }

    @Override
    public List<MenuResource> getResourceInfoByRoleId(Long roleId) {
        List<RoleResource> roleResources = this.findByRoleIdAndStatus(roleId, null);
        if (CollectionUtils.isEmpty(roleResources)) {
            List<Long> resourceIds = roleResources.stream().map(RoleResource::getResourceId).collect(Collectors.toList());
            return this.menuResourceMongoService.findAllByIdIn(resourceIds);
        }
        return null;
    }

    @Override
    public List<MenuResource> getResourceInfoByRoleIdIn(List<Long> roleId) {
        List<RoleResource> roleResources = this.findByRoleIdInAndStatus(roleId, null);
        if (CollectionUtils.isEmpty(roleResources)) {
            List<Long> resourceIds = roleResources.stream().map(RoleResource::getResourceId).distinct().collect(Collectors.toList());
            return this.menuResourceMongoService.findAllByIdIn(resourceIds);
        }
        return null;
    }


    @Transactional(
            value = "mongoTransactionManager",
            rollbackFor = {RuntimeException.class, Exception.class}
    )
    @Override
    public long deleteByRoleIdIn(List<Long> roleIds) {
        return this.roleResourceMongoRepository.deleteByRoleIdIn(roleIds);
    }

    @Transactional(
            value = "mongoTransactionManager",
            rollbackFor = {RuntimeException.class, Exception.class}
    )

    @Override
    public long deleteByResourceIdIn(List<Long> resourceIds) {
        return this.roleResourceMongoRepository.deleteByResourceIdIn(resourceIds);
    }

    /**
     * 构建 ztree
     * @param list
     * @return
     */
    private List<ZtreeNode> startBuilderZtree( List<MenuResource> list, Map<Long, RoleResource> roleResourceMap){
        List<ZtreeNode> treeNodes = new LinkedList<>();
        if (!CollectionUtils.isEmpty(list)){
            list.stream().forEach(item -> {
                ZtreeNode zTreeNode = new ZtreeNode(item.getId(), item.getParentId(), item.getMenuName());
                if (roleResourceMap.get(item.getId()) != null) {
                    zTreeNode.setChecked(true);
                }
                Map<String, String> attributesMap = new ConcurrentHashMap<>(2);
                attributesMap.put("fullParent", item.getFullMenuParent());
                attributesMap.put("menuNumber", item.getMenuNumber());
                attributesMap.put("menuClassify", item.getMenuClassify().toString());
                zTreeNode.setOtherAttributes(attributesMap);
                treeNodes.add(zTreeNode);
            });
        }
        return ZtreeBuilder.buildListToTree(treeNodes);
    }

}
