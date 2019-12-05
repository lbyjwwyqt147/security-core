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
import pers.liujunyi.cloud.security.domain.authorization.PositionInfoQueryDto;
import pers.liujunyi.cloud.security.entity.authorization.PositionInfo;
import pers.liujunyi.cloud.security.repository.mongo.authorization.PositionInfoMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.PositionInfoMongoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/***
 * 文件名称: PositionInfoMongoServiceImpl.java
 * 文件描述: 岗位 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class PositionInfoMongoServiceImpl extends BaseMongoServiceImpl<PositionInfo, Long> implements PositionInfoMongoService {

    @Autowired
    private PositionInfoMongoRepository positionInfoMongoRepository;

    public PositionInfoMongoServiceImpl(BaseMongoRepository<PositionInfo, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }

    @Override
    public PositionInfo findFirstByPostNumber(String postNumber) {
        return this.positionInfoMongoRepository.findFirstByPostNumber(postNumber);
    }

    @Override
    public List<ZtreeNode> positionTree(Long pid, Byte status) {
        List<PositionInfo> list = null;
        if (pid != null) {
            if (status != null) {
                list = this.positionInfoMongoRepository.findByParentIdAndPostStatusOrderBySerialNumberAsc(pid,  status);
            } else {
                list = this.positionInfoMongoRepository.findByParentIdOrderBySerialNumberAsc(pid);
            }
        } else {
             list = this.positionInfoMongoRepository.findAll(Sort.by(Sort.Direction.ASC, "serialNumber"));
        }
        return this.startBuilderZtree(list);
    }

    @Override
    public List<ZtreeNode> positionFullParentCodeTree(String fullParentCode) {
        List<PositionInfo> list = this.positionInfoMongoRepository.findByFullPostParentCodeLikeAndPostStatusOrderBySerialNumberAsc(fullParentCode,  SecurityConstant.ENABLE_STATUS);
        return this.startBuilderZtree(list);
    }

    @Override
    public ResultInfo findPageGird(PositionInfoQueryDto query) {
        Sort sort = Sort.by(Sort.Direction.ASC, "serialNumber");
        Pageable pageable = query.toPageable(sort);
        // 查询条件
        Query searchQuery = query.toSpecPageable(pageable);
        // 查询总记录条数
        long totalElements = this.mongoDbTemplate.count(searchQuery, PositionInfo.class);
        // 查询数据
        List<PositionInfo> searchPageResults =  this.mongoDbTemplate.find(searchQuery, PositionInfo.class);
        ResultInfo result = ResultUtil.success(searchPageResults, super.secretKey);
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public String getPositionName(Long id) {
        PositionInfo positionInfo = this.findById(id);
        if (positionInfo != null) {
            return positionInfo.getPostName();
        }
        return null;
    }


    @Override
    public ResultInfo selectById(Long id) {
        return ResultUtil.success(this.findById(id));
    }

    @Override
    public Map<Long, String> positionNameToMap(List<Long> ids) {
        List<PositionInfo> list = this.positionInfoMongoRepository.findByIdIn(ids);
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().collect(Collectors.toMap(PositionInfo::getId, PositionInfo::getPostName));
        }
        return null;
    }

    @Override
    public PositionInfo findById(Long id) {
        Optional<PositionInfo> positionInfo = this.positionInfoMongoRepository.findById(id);
        if (positionInfo.isPresent()) {
            return positionInfo.get();
        }
        return null;
    }


    /**
     * 构建 ztree
     * @param list
     * @return
     */
    private List<ZtreeNode> startBuilderZtree( List<PositionInfo> list){
        List<ZtreeNode> treeNodes = new LinkedList<>();
        if (!CollectionUtils.isEmpty(list)){
            list.stream().forEach(item -> {
                ZtreeNode zTreeNode = new ZtreeNode(item.getId(), item.getParentId(), item.getPostName());
                Map<String, String> attributesMap = new ConcurrentHashMap<>(2);
                attributesMap.put("fullParent", item.getFullPostParent());
                attributesMap.put("postNumber", item.getPostNumber());
                zTreeNode.setOtherAttributes(attributesMap);
                treeNodes.add(zTreeNode);
            });
        }
        return ZtreeBuilder.buildListToTree(treeNodes);
    }
}
