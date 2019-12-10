package pers.liujunyi.cloud.security.service.organizations.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.encrypt.AesEncryptUtils;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseMongoServiceImpl;
import pers.liujunyi.cloud.common.vo.tree.ZtreeBuilder;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.organizations.OrganizationsQueryDto;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;
import pers.liujunyi.cloud.security.repository.mongo.organizations.OrganizationsMongoRepository;
import pers.liujunyi.cloud.security.service.organizations.OrganizationsMongoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/***
 * 文件名称: OrganizationsMongoServiceImpl.java
 * 文件描述: 组织结构 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class OrganizationsMongoServiceImpl extends BaseMongoServiceImpl<Organizations, Long> implements OrganizationsMongoService {

    @Autowired
    private OrganizationsMongoRepository organizationsMongoRepository;

    public OrganizationsMongoServiceImpl(BaseMongoRepository<Organizations, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }


    @Override
    public Organizations findFirstByOrgNumber(String orgNumber) {
        return this.organizationsMongoRepository.findFirstByOrgNumber(orgNumber);
    }

    @Override
    public List<ZtreeNode> orgTree(Long pid, Byte status) {
        List<Organizations> list = null;
        if (pid != null) {
            if (status != null) {
                list = this.organizationsMongoRepository.findByParentIdAndOrgStatusOrderBySeqAsc(pid,  status);
            } else {
                list = this.organizationsMongoRepository.findByParentIdOrderBySeqAsc(pid);
            }
        } else {
            list =  this.organizationsMongoRepository.findAll(Sort.by(Sort.Direction.ASC, "seq"));
        }
        return this.startBuilderZtree(list);
    }

    @Override
    public List<ZtreeNode> orgFullParentCodeTree(String fullParentCode) {
        List<Organizations> list = this.organizationsMongoRepository.findByFullParentCodeLikeAndOrgStatusOrderBySeqAsc(fullParentCode,  SecurityConstant.ENABLE_STATUS);
        return this.startBuilderZtree(list);
    }

    @Override
    public ResultInfo findPageGird(OrganizationsQueryDto query) {
        Sort sort = Sort.by(Sort.Direction.ASC, "seq");
        Pageable pageable = query.toPageable(sort);
        // 查询条件
        Query searchQuery = query.toSpecPageable(pageable);
        // 查询总记录条数
        long totalElements = this.mongoDbTemplate.count(searchQuery, Organizations.class);
        // 查询数据
        List<Organizations> searchPageResults =  this.mongoDbTemplate.find(searchQuery, Organizations.class);
        if (!CollectionUtils.isEmpty(searchPageResults)) {
            searchPageResults.stream().forEach(item -> {
                String fullName = this.getOrgFullName(item.getId());
                item.setFullName(fullName);
            });
        }
        ResultInfo result = ResultUtil.success(AesEncryptUtils.aesEncrypt(searchPageResults, super.secretKey));
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public String getOrgFullName(Long id) {
        StringBuffer fullName = new StringBuffer();
        Organizations organizations = this.getOrganizations(id);
        if (organizations != null) {
            if (StringUtils.isNotBlank(organizations.getFullParent())) {
                List<Long> parentIds = new LinkedList<>();
                String[] parentIdArray = organizations.getFullParent().split(":");
                for (String parentId : parentIdArray) {
                    parentIds.add(Long.valueOf(parentId));
                }
                Map<Long, String> orgNameMap = this.getOrgNameMap(parentIds);
                if (!CollectionUtils.isEmpty(orgNameMap)) {
                    for (String orgName : orgNameMap.values()) {
                        fullName.append(orgName).append("-");
                    }
                }
            }
            fullName.append(organizations.getOrgName());
        }
        return fullName.toString();
    }


    @Override
    public Map<Long, String> findKeyIdValueNameByIdIn(List<Long> ids) {
        List<Organizations> list = this.organizationsMongoRepository.findByIdIn(ids);
        if (!CollectionUtils.isEmpty(list)) {
            return  list.stream().collect(Collectors.toMap(Organizations::getId, Organizations::getOrgName));
        }
        return null;
    }

    @Override
    public ResultInfo selectById(Long id) {
        Organizations  search =  this.getOrganizations(id);
        if (search != null) {
            return ResultUtil.success(search);
        }
        return ResultUtil.fail();
    }


    @Override
    public String getFullOrgName(Long id) {
        String fullOrgName = null;
        Organizations organizations = this.getOrganizations(id);
        if (organizations != null){
            if (organizations.getFullParent().equals("0")) {
                fullOrgName = organizations.getOrgName();
            } else {
                fullOrgName = this.buildFullParenOrgName(organizations.getFullParent());
                fullOrgName = fullOrgName + organizations.getOrgName();
            }
        }
        return fullOrgName;
    }

    @Override
    public String getOrgName(Long id) {
        Organizations organizations = this.getOrganizations(id);
        if (organizations != null) {
            return organizations.getOrgName();
        }
        return null;
    }

    @Override
    public Map<Long, String> fullOrgNameToMap(List<Long> ids) {
        Map<Long, String> fullOrgNameMap = new ConcurrentHashMap<>();
        List<Organizations> list = this.organizationsMongoRepository.findByIdIn(ids);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(item -> {
                String fullOrgName = "";
                if (item.getFullParent().equals("0")) {
                    fullOrgName = item.getOrgName();
                } else {
                    fullOrgName = this.buildFullParenOrgName(item.getFullParent());
                    fullOrgName = fullOrgName + item.getOrgName();
                }
                fullOrgNameMap.put(item.getId(), fullOrgName);
            });
        }
        return fullOrgNameMap;
    }

    @Override
    public Map<Long, String> orgNameToMap(List<Long> ids) {
        List<Organizations> list = this.organizationsMongoRepository.findByIdIn(ids);
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().collect(Collectors.toMap(Organizations::getId, Organizations::getOrgName));
        }
        return null;
    }

    /**
     * 根据id 获取数据
     * @param id
     * @return
     */
    private Organizations getOrganizations(Long id) {
        Optional<Organizations> organizations = this.organizationsMongoRepository.findById(id);
        if (organizations.isPresent()) {
            return organizations.get();
        }
        return null;
    }


    /**
     * 根据一组id 获取结构名称
     * @param ids
     * @return key = id  value = name
     */
    private Map<Long, String> getOrgNameMap(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<Organizations> list = this.organizationsMongoRepository.findByIdIn(ids);
            if (!CollectionUtils.isEmpty(list)) {
                return  list.stream().collect(Collectors.toMap(Organizations::getId, Organizations::getOrgName));
            }
            return null;
        }
        return null;
    }

    /**
     * 构建 ztree
     * @param list
     * @return
     */
    private List<ZtreeNode> startBuilderZtree( List<Organizations> list){
        List<ZtreeNode> treeNodes = new LinkedList<>();
        if (!CollectionUtils.isEmpty(list)){
            list.stream().forEach(item -> {
                ZtreeNode zTreeNode = new ZtreeNode(item.getId(), item.getParentId(), item.getOrgName());
                Map<String, String> attributesMap = new ConcurrentHashMap<>();
                attributesMap.put("fullParent", item.getFullParent());
                attributesMap.put("orgNumber", item.getOrgNumber());
                zTreeNode.setOtherAttributes(attributesMap);
                treeNodes.add(zTreeNode);
            });
        }
        return ZtreeBuilder.buildListToTree(treeNodes);
    }

    /**
     * 获取组织结构 全名称
     * @param fullParent
     * @return
     */
    private String buildFullParenOrgName(String fullParent){
        StringBuffer fullParenOrgNameBuffer = new StringBuffer();
        String[] parentIds = fullParent.split(":");
        List<Long> parentIdList = new ArrayList<>();
        for (String pid : parentIds) {
            if (StringUtils.isNotBlank(pid) && !pid.equals("0")) {
                parentIdList.add(Long.valueOf(pid));
            }
        }
        if (!CollectionUtils.isEmpty(parentIdList)) {
            List<Organizations> list = this.organizationsMongoRepository.findByIdInOrderByIdAsc(parentIdList);
            list.stream().forEach(item -> {
                fullParenOrgNameBuffer.append(item.getOrgName()).append("-");
            });
        }
        String fullParenOrgName = null;
        if (fullParenOrgNameBuffer.length() > 0) {
            fullParenOrgName = fullParenOrgName.toString();
        }
        return fullParenOrgName;
    }
}
