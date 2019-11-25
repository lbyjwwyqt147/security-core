package pers.liujunyi.cloud.security.service.organizations.impl;


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
import pers.liujunyi.cloud.security.domain.organizations.StaffOrgQueryDto;
import pers.liujunyi.cloud.security.domain.organizations.StaffOrgVo;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.repository.mongo.organizations.StaffOrgMongoRepository;
import pers.liujunyi.cloud.security.service.organizations.OrganizationsMongoService;
import pers.liujunyi.cloud.security.service.organizations.StaffOrgMongoService;
import pers.liujunyi.cloud.security.service.user.UserAccountsMongoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


/***
 * 文件名称: StaffOrgMongoServiceImpl.java
 * 文件描述: 职工关联组织机构 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class StaffOrgMongoServiceImpl extends BaseMongoServiceImpl<StaffOrg, Long> implements StaffOrgMongoService {

    @Autowired
    private StaffOrgMongoRepository staffOrgMongoRepository;
    @Autowired
    private UserAccountsMongoService userAccountsMongoService;
    @Autowired
    private OrganizationsMongoService organizationsMongoService;

    public StaffOrgMongoServiceImpl(BaseMongoRepository<StaffOrg, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }


    @Override
    public List<StaffOrg> findByOrgIdAndStatus(Long orgId, Byte status) {
        return this.staffOrgMongoRepository.findByOrgIdAndStatus(orgId, status);
    }

    @Override
    public List<StaffOrg> findByOrgIdIn(List<Long> orgIds) {
        return this.staffOrgMongoRepository.findByOrgIdIn(orgIds);
    }

    @Override
    public List<StaffOrg> findByOrgIdInOrderByIdAsc(List<Long> orgIds) {
        return this.staffOrgMongoRepository.findByOrgIdInOrderByIdAsc(orgIds);
    }

    @Override
    public List<StaffOrg> findByStaffIdAndStatus(Long staffId, Byte status) {
        return this.staffOrgMongoRepository.findByStaffIdAndStatus(staffId, status);
    }

    @Override
    public ResultInfo findPageGird(StaffOrgQueryDto query) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = query.toPageable(sort);
        // 查询条件
        Query searchQuery = query.toSpecPageable(pageable);
        // 查询总记录条数
        long totalElements = this.mongoDbTemplate.count(searchQuery, StaffOrg.class);
        // 查询数据
        List<StaffOrg> searchPageResults =  this.mongoDbTemplate.find(searchQuery, StaffOrg.class);
        List<StaffOrgVo> resultDataList = new CopyOnWriteArrayList<>();
        if (!CollectionUtils.isEmpty(searchPageResults)) {
            List<Long> staffIds = searchPageResults.stream().map(StaffOrg::getStaffId).collect(Collectors.toList());
            Map<Long, UserAccounts> userMap = this.userAccountsMongoService.getUserAccountInfoToMap(staffIds);
            searchPageResults.stream().forEach(item -> {
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
        ResultInfo result = ResultUtil.success(AesEncryptUtils.aesEncrypt(resultDataList, super.secretKey));
        result.setTotal(totalElements);
        return  result;
    }

    @Override
    public List<StaffOrg> findByStaffIdIn(List<Long> staffIds) {
        return this.staffOrgMongoRepository.findByStaffIdIn(staffIds);
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

    @Override
    public List<Organizations> getOrgInfoByStaffId(Long staffId) {
        List<StaffOrg> staffOrgList = this.findByStaffIdAndStatus(staffId, SecurityConstant.ENABLE_STATUS);
        if (!CollectionUtils.isEmpty(staffOrgList)) {
            List<Long> orgIds = staffOrgList.stream().map(StaffOrg::getOrgId).distinct().collect(Collectors.toList());
            // 获取机构信息
            List<Organizations> organizationsList = this.organizationsMongoService.findByIdIn(orgIds);
            return organizationsList;
        }
        return null;
    }

    @Override
    public Map<Long, List<Organizations>> getOrgInfoByStaffIdIn(List<Long> staffId) {
        Map<Long, List<Organizations>> orgMap = new ConcurrentHashMap<>();
        List<StaffOrg> staffOrgList = this.findByStaffIdIn(staffId);
        if (!CollectionUtils.isEmpty(staffOrgList)) {
            Map<Long, List<StaffOrg>> groupMap = staffOrgList.stream().collect(Collectors.groupingBy(StaffOrg::getStaffId));
            for (Map.Entry<Long, List<StaffOrg>> entry : groupMap.entrySet()) {
                List<Long> orgIds = entry.getValue().stream().map(StaffOrg::getOrgId).distinct().collect(Collectors.toList());
                // 获取机构信息
                List<Organizations> organizationsList = this.organizationsMongoService.findByIdIn(orgIds);
                orgMap.put(entry.getKey(), organizationsList);
            }
        }
        return orgMap;
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
        List<StaffOrg> list = this.staffOrgMongoRepository.findByStaffId(staffId);
        if (!CollectionUtils.isEmpty(list)) {
            List<Long> orgIds = list.stream().map(StaffOrg::getOrgId).distinct().collect(Collectors.toList());
            Map<Long, String> orgNameMap = null;
            if (full) {
                orgNameMap = this.organizationsMongoService.fullOrgNameToMap(orgIds);
            } else {
                orgNameMap = this.organizationsMongoService.orgNameToMap(orgIds);
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
