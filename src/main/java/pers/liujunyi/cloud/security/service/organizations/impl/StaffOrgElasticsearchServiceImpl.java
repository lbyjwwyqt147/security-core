package pers.liujunyi.cloud.security.service.organizations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.security.domain.organizations.StaffOrgQueryDto;
import pers.liujunyi.cloud.security.domain.organizations.StaffOrgVo;
import pers.liujunyi.cloud.security.entity.organizations.Organizations;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.repository.elasticsearch.organizations.StaffOrgElasticsearchRepository;
import pers.liujunyi.cloud.security.service.organizations.StaffOrgElasticsearchService;
import pers.liujunyi.cloud.security.service.user.UserAccountsElasticsearchService;

import java.util.List;
import java.util.Map;
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

    public StaffOrgElasticsearchServiceImpl(BaseElasticsearchRepository<StaffOrg, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


    @Override
    public List<StaffOrg> findByOrgIdAndStatus(Long orgId, Byte status) {
        return this.staffOrgElasticsearchRepository.findByOrgIdAndStatus(orgId, status, super.allPageable);
    }

    @Override
    public List<StaffOrg> findByStaffIdAndStatus(Long staffId, Byte status) {
        return this.staffOrgElasticsearchRepository.findByStaffIdAndStatus(staffId, status, super.allPageable);
    }

    @Override
    public ResultInfo findPageGird(StaffOrgQueryDto query) {
        // 排序方式
        Sort sort =  new Sort(Sort.Direction.ASC, "id");
        //分页参数
        Pageable pageable = query.toPageable(sort);
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
        ResultInfo result = ResultUtil.success(resultDataList);
        result.setTotal(totalElements);
        return  result;
    }

}
