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
import pers.liujunyi.cloud.security.domain.authorization.MenuResourceQueryDto;
import pers.liujunyi.cloud.security.entity.authorization.MenuResource;
import pers.liujunyi.cloud.security.repository.mongo.authorization.MenuResourceMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.MenuResourceMongoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: MenuResourceMongoServiceImpl.java
 * 文件描述: 菜单资源 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class MenuResourceMongoServiceImpl extends BaseMongoServiceImpl<MenuResource, Long>  implements MenuResourceMongoService {

    @Autowired
    protected MenuResourceMongoRepository menuResourceMongoRepository;

    public MenuResourceMongoServiceImpl(BaseMongoRepository<MenuResource, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }

    @Override
    public MenuResource findFirstByMenuNumber(String menuNumber) {
        return this.menuResourceMongoRepository.findFirstByMenuNumber(menuNumber);
    }

    @Override
    public List<ZtreeNode> menuResourceTree(Long pid, Byte status) {
        List<MenuResource> list = null;
        if (pid != null) {
            if (status != null) {
                list = this.menuResourceMongoRepository.findByParentIdAndMenuStatusOrderBySerialNumberAsc(pid,  status);
            } else {
                list = this.menuResourceMongoRepository.findByParentIdOrderBySerialNumberAsc(pid);
            }
        } else {
            list =  this.menuResourceMongoRepository.findAll(Sort.by(Sort.Direction.ASC, "serialNumber"));
        }
        return this.startBuilderZtree(list);
    }

    @Override
    public List<ZtreeNode> menuResourceFullParentCodeTree(String fullParentCode) {
        List<MenuResource> list = this.menuResourceMongoRepository.findByFullMenuParentCodeLikeAndMenuStatusOrderBySerialNumberAsc(fullParentCode,  SecurityConstant.ENABLE_STATUS);
        return this.startBuilderZtree(list);
    }

    @Override
    public ResultInfo findPageGird(MenuResourceQueryDto query) {
        Sort sort = Sort.by(Sort.Direction.ASC, "serialNumber");
        Pageable pageable = query.toPageable(sort);
        // 查询条件
        Query searchQuery = query.toSpecPageable(pageable);
        // 查询总记录条数
        long totalElements = this.mongoDbTemplate.count(searchQuery, MenuResource.class);
        // 查询数据
        List<MenuResource> searchPageResults =  this.mongoDbTemplate.find(searchQuery, MenuResource.class);
        ResultInfo result = ResultUtil.success(searchPageResults, super.secretKey);
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public ResultInfo selectById(Long id) {
        return ResultUtil.success(this.findById(id));
    }

    @Override
    public MenuResource findById(Long id) {
        Optional<MenuResource> menuResource = this.menuResourceMongoRepository.findById(id);
        if (menuResource.isPresent()) {
            return menuResource.get();
        }
        return null;
    }

    @Override
    public List<MenuResource> findByParentIdAndMenuNumber(Long pid, String menuNumber) {
        return this.menuResourceMongoRepository.findByParentIdAndMenuNumber(pid, menuNumber);
    }


    /**
     * 构建 ztree
     * @param list
     * @return
     */
    private List<ZtreeNode> startBuilderZtree( List<MenuResource> list){
        List<ZtreeNode> treeNodes = new LinkedList<>();
        if (!CollectionUtils.isEmpty(list)){
            list.stream().forEach(item -> {
                ZtreeNode zTreeNode = new ZtreeNode(item.getId(), item.getParentId(), item.getMenuName());
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
