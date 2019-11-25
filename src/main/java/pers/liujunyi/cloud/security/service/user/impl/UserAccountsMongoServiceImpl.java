package pers.liujunyi.cloud.security.service.user.impl;


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
import pers.liujunyi.cloud.security.domain.user.UserAccountsQueryDto;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.repository.mongo.user.UserAccountsMongoRepository;
import pers.liujunyi.cloud.security.service.user.UserAccountsMongoService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/***
 * 文件名称: UserAccountsMongoServiceImpl.java
 * 文件描述: 用户帐号信息 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class UserAccountsMongoServiceImpl extends BaseMongoServiceImpl<UserAccounts, Long> implements UserAccountsMongoService {

    @Autowired
    private UserAccountsMongoRepository userAccountsMongoRepository;

    public UserAccountsMongoServiceImpl(BaseMongoRepository<UserAccounts, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }


    @Override
    public ResultInfo userLogin(String userAccounts, String userPassword) {
        UserAccounts accounts = this.userAccountsMongoRepository.findFirstByUserAccountsOrMobilePhoneOrUserNumberAndUserPassword(userAccounts, userAccounts, userAccounts, userPassword);
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
        return this.userAccountsMongoRepository.findFirstByUserAccountsOrMobilePhoneOrUserNumber(userAccounts, userAccounts, userAccounts);
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
        Sort sort = Sort.by(Sort.Direction.DESC, "registrationTime");
        Pageable pageable = query.toPageable(sort);
        // 查询条件
        Query searchQuery = query.toSpecPageable(pageable);
        // 查询总记录条数
        long totalElements = this.mongoDbTemplate.count(searchQuery, StaffOrg.class);
        // 查询数据
        List<StaffOrg> searchPageResults =  this.mongoDbTemplate.find(searchQuery, StaffOrg.class);
        ResultInfo result = ResultUtil.success(AesEncryptUtils.aesEncrypt(searchPageResults, super.secretKey));
        result.setTotal(totalElements);
        return  result;
    }
}
