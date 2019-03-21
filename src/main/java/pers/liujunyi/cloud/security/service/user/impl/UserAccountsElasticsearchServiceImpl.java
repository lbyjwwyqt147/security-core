package pers.liujunyi.cloud.security.service.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.repository.elasticsearch.user.UserAccountsElasticsearchRepository;
import pers.liujunyi.cloud.security.service.user.UserAccountsElasticsearchService;


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
        return null;
    }

    @Override
    public UserAccounts findFirstByUserAccountsOrMobilePhoneOrUserNumber(String userAccounts) {
        return null;
    }
}
