package pers.liujunyi.cloud.security.service.organizations.impl;

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
import pers.liujunyi.cloud.security.domain.organizations.StaffOrgQueryDto;
import pers.liujunyi.cloud.security.domain.organizations.StaffOrgVo;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.repository.elasticsearch.organizations.StaffOrgElasticsearchRepository;
import pers.liujunyi.cloud.security.service.organizations.OrganizationsElasticsearchService;
import pers.liujunyi.cloud.security.service.organizations.StaffOrgElasticsearchService;
import pers.liujunyi.cloud.security.service.user.UserAccountsElasticsearchService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


/***
 * 文件名称: StaffOrgElasticsearchServiceImpl.java
 * 文件描述: 职工关联组织机构 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class StaffOrgElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<StaffOrg, Long> implements StaffOrgElasticsearchService {

    @Autowired
    private StaffOrgElasticsearchRepository staffOrgElasticsearchRepository;
    @Autowired
    private UserAccountsElasticsearchService userAccountsElasticsearchService;
    @Autowired
    private OrganizationsElasticsearchService organizationsElasticsearchService;

    public StaffOrgElasticsearchServiceImpl(BaseElasticsearchRepository<StaffOrg, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


    @Override
    public List<StaffOrg> findByOrgIdAndStatus(Long orgId, Byte status) {
        return this.staffOrgElasticsearchRepository.findByOrgIdAndStatus(orgId, status, super.allPageable);
    }

    @Override
    public List<StaffOrg> findByOrgIdIn(List<Long> orgIds) {
        return this.staffOrgElasticsearchRepository.findByOrgIdIn(orgIds, super.getPageable(orgIds.size()));
    }

    @Override
    public List<StaffOrg> findByOrgIdInOrderByIdAsc(List<Long> orgIds) {
        return this.staffOrgElasticsearchRepository.findByOrgIdInOrderByIdAsc(orgIds, super.getPageable(orgIds.size()));
    }

    @Override
    public List<StaffOrg> findByStaffIdAndStatus(Long staffId, Byte status) {
        return this.staffOrgElasticsearchRepository.findByStaffIdAndStatus(staffId, status, super.allPageable);
    }

    @Override
    public ResultInfo findPageGird(StaffOrgQueryDto query) {
        //分页参数
        Pageable pageable = query.toPageable(Sort.Direction.DESC, "updateTime");
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<StaffOrg> searchPageResults = this.staffOrgElasticsearchRepository.search(searchQuery);
        List<StaffOrgVo> resultDataList = new CopyOnWriteArrayList<>();
        List<StaffOrg> searchDataList = searchPageResults.getContent();
        if (!CollectionUtils.isEmpty(searchDataList)) {
            List<Long> staffIds = searchDataList.stream().map(StaffOrg::getStaffId).collect(Collectors.toList());
            Map<Long, UserAccounts> userMap = this.userAccountsElasticsearchService.getUserAccountInfoToMap(staffIds);
            searchDataList.stream().forEach(item -> {
                StaffOrgVo staffOrgVo = new StaffOrgVo();
                staffOrgVo.setId(item.getId());
                staffOrgVo.setOrgId(item.getOrgId());
                staffOrgVo.setStaffId(item.getStaffId());
                staffOrgVo.setStatus(item.getStatus());
                if (!CollectionUtils.isEmpty(userMap)) {
                    UserAccounts userAccounts = userMap.get(item.getId());
                    staffOrgVo.setUserName(userAccounts != null ? userAccounts.getUserName() : "");
                    staffOrgVo.setUserNumber(userAccounts != null ? userAccounts.getUserNumber() : "");
                    staffOrgVo.setMobilePhone(userAccounts != null ? userAccounts.getMobilePhone() : "");
                }
                resultDataList.add(staffOrgVo);
            });
        }
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(AesEncryptUtils.aesEncrypt(resultDataList, super.secretKey));
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public List<StaffOrg> findByStaffIdIn(List<Long> staffIds) {
        return this.staffOrgElasticsearchRepository.findByStaffIdIn(staffIds, super.getPageable(staffIds.size()));
    }

    @Override
    public String getFullOrgName(Long staffId) {
        return this.getOrgName(staffId, true);
    }

    @Override
    public String getOrgName(Long staffId) {
        return this.getOrgName(staffId, false);
    }

    @Override
    public Map<Long, String> fullOrgNameToMap(List<Long> staffId) {
        return this.getOrgNameToMap(staffId, true);
    }

    @Override
    public Map<Long, String> orgNameToMap(List<Long> staffId) {
        return this.getOrgNameToMap(staffId, false);
    }

    /**
     * 获取人员机构名称
     * @param staffId
     * @param full
     * @return
     */
    private Map<Long, String> getOrgNameToMap(List<Long> staffId, Boolean full) {
        Map<Long, String> orgNameMap = new ConcurrentHashMap<>();
        List<StaffOrg> list = this.findByStaffIdIn(staffId);
        if (!CollectionUtils.isEmpty(list)) {
            list.stream().forEach(item -> {
                orgNameMap.put(item.getStaffId(), this.getOrgName(item.getStaffId(), full));
            });
        }
        return orgNameMap;
    }


    /**
     * 获取人员机构名称
     * @param staffId
     * @param full
     * @return
     */
    private String getOrgName(Long staffId,  Boolean full) {
        StringBuffer orgNameBuffer = new StringBuffer();
        List<StaffOrg> list = this.staffOrgElasticsearchRepository.findByStaffId(staffId, super.allPageable);
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> orgIds = list.stream().map(StaffOrg::getOrgId).distinct().collect(Collectors.toList());
            Map<Long, String> orgNameMap = null;
            if (full) {
                orgNameMap = this.organizationsElasticsearchService.fullOrgNameToMap(orgIds);
            } else {
                orgNameMap = this.organizationsElasticsearchService.orgNameToMap(orgIds);
            }
            if (!CollectionUtils.isEmpty(orgNameMap)) {
                final Map<Long, String> map = orgNameMap;
                orgIds.stream().forEach(item -> {
                    if (map.get(item) != null) {
                        orgNameBuffer.append(map.get(item)).append(";");
                    }
                });
            }
        }
        return orgNameBuffer.toString();
    }

}
