package pers.liujunyi.cloud.security.service.organizations.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.encrypt.AesEncryptUtils;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.common.vo.tree.ZtreeBuilder;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.organizations.OrganizationsQueryDto;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;
import pers.liujunyi.cloud.security.repository.elasticsearch.organizations.OrganizationsElasticsearchRepository;
import pers.liujunyi.cloud.security.service.organizations.OrganizationsElasticsearchService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/***
 * 文件名称: OrganizationsElasticsearchServiceImpl.java
 * 文件描述: 组织机构 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class OrganizationsElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<Organizations, Long> implements OrganizationsElasticsearchService {

    @Autowired
    private OrganizationsElasticsearchRepository organizationsElasticsearchRepository;

    public OrganizationsElasticsearchServiceImpl(BaseElasticsearchRepository<Organizations, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


    @Override
    public List<ZtreeNode> orgTree(Long pid, Byte status) {
        List<Organizations> list = this.organizationsElasticsearchRepository.findByParentIdAndOrgStatusOrderBySeqAsc(pid,  status, super.allPageable);
        return this.startBuilderZtree(list);
    }

    @Override
    public List<ZtreeNode> orgFullParentCodeTree(String fullParentCode) {
        List<Organizations> list = this.organizationsElasticsearchRepository.findByFullParentCodeLikeAndOrgStatusOrderBySeqAsc(fullParentCode,  SecurityConstant.ENABLE_STATUS, super.allPageable);
        return this.startBuilderZtree(list);
    }

    @Override
    public ResultInfo findPageGird(OrganizationsQueryDto query) {
        // 排序方式
        Sort sort =  new Sort(Sort.Direction.ASC, "seq");
        //分页参数
        Pageable pageable = query.toPageable(sort);
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<Organizations> searchPageResults = this.organizationsElasticsearchRepository.search(searchQuery);
        List<Organizations> searchDataList = searchPageResults.getContent();
        searchDataList.stream().forEach(item -> {
            String fullName = this.getOrgFullName(item.getId());
            item.setFullName(fullName);
        });
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(AesEncryptUtils.aesEncrypt(searchDataList, super.secretKey));
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
        List<Organizations> list = this.organizationsElasticsearchRepository.findByIdIn(ids, super.getPageable(ids.size()));
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
        List<Organizations> list = this.organizationsElasticsearchRepository.findByIdIn(ids, super.getPageable(ids.size()));
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
        List<Organizations> list = this.organizationsElasticsearchRepository.findByIdIn(ids, super.getPageable(ids.size()));
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
        Optional<Organizations> organizations = this.organizationsElasticsearchRepository.findById(id);
        if (organizations.isPresent()) {
            return organizations.get();
        }
        return null;
    }


    /**
     * 根据一组id 获取机构名称
     * @param ids
     * @return key = id  value = name
     */
    private Map<Long, String> getOrgNameMap(List<Long> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<Organizations> list = this.organizationsElasticsearchRepository.findByIdIn(ids, super.getPageable(ids.size()));
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
                treeNodes.add(zTreeNode);
            });
        }
        return ZtreeBuilder.buildListToTree(treeNodes);
    }

    /**
     * 获取组织机构 全名称
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
            List<Organizations> list = this.organizationsElasticsearchRepository.findByIdInOrderByIdAsc(parentIdList, super.getPageable(parentIdList.size()));
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
