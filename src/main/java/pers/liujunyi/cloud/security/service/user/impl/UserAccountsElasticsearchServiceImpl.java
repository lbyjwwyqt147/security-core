package pers.liujunyi.cloud.security.service.user.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.encrypt.AesEncryptUtils;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.security.domain.user.UserAccountsQueryDto;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.repository.elasticsearch.user.UserAccountsElasticsearchRepository;
import pers.liujunyi.cloud.security.service.user.UserAccountsElasticsearchService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/***
 * 文件名称: UserAccountsElasticsearchServiceImpl.java
 * 文件描述: 用户帐号信息 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class UserAccountsElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<UserAccounts, Long> implements UserAccountsElasticsearchService {

    @Autowired
    private UserAccountsElasticsearchRepository userAccountsElasticsearchRepository;

    public UserAccountsElasticsearchServiceImpl(BaseElasticsearchRepository<UserAccounts, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


    @Override
    public ResultInfo userLogin(String userAccounts, String userPassword) {
        UserAccounts accounts = this.userAccountsElasticsearchRepository.findFirstByUserAccountsOrMobilePhoneOrUserNumberAndUserPassword(userAccounts, userAccounts, userAccounts, userPassword);
        if (accounts != null) {
            if (accounts.getUserStatus().byteValue() == 1) {
                return ResultUtil.params("账户已被禁用,请联系客服人员");
            }
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public UserAccounts findFirstByUserAccountsOrMobilePhoneOrUserNumber(String userAccounts) {
        return this.userAccountsElasticsearchRepository.findFirstByUserAccountsOrMobilePhoneOrUserNumber(userAccounts, userAccounts, userAccounts);
    }

    @Override
    public Map<Long, UserAccounts> getUserAccountInfoToMap(List<Long> ids) {
        List<UserAccounts> list = this.findByIdIn(ids);
        if (!CollectionUtils.isEmpty(list)) {
          Map<Long, UserAccounts> map =  list.stream().collect(Collectors.toMap(UserAccounts::getId, UserAccounts -> UserAccounts));
          return  map;
        }
        return null;
    }

    @Override
    public Map<Long, String> getUserNameToMap(List<Long> ids) {
        List<UserAccounts> list = this.findByIdIn(ids);
        if (!CollectionUtils.isEmpty(list)) {
            Map<Long, String> map = new ConcurrentHashMap<>();
            list.stream().forEach(item -> {
                map.put(item.getId(), StringUtils.isNotBlank(item.getUserName()) ? item.getUserName() : item.getUserNickName());
            });
            return  map;
        }
        return null;
    }

    @Override
    public ResultInfo findPageGird(UserAccountsQueryDto query) {
        // 排序方式 解决无数据时异常 No mapping found for [registrationTime] in order to sort on
        SortBuilder sortBuilder = SortBuilders.fieldSort("registrationTime").unmappedType("date").order(SortOrder.DESC);
        // 如果使用这种排序方式 如果表中数据为空时,会报异常 No mapping found for [createTime] in order to sort on
        //Sort sort = Sort.by(Sort.Direction.DESC, "registrationTime");
        // 查询数据
        SearchQuery searchQuery = query.toSpecSortPageable(sortBuilder);
        Page<UserAccounts> searchPageResults = this.userAccountsElasticsearchRepository.search(searchQuery);
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(AesEncryptUtils.aesEncrypt(searchPageResults.getContent(), super.secretKey));
        result.setTotal(totalElements);
        return  result;
    }
}
