package pers.liujunyi.cloud.security.service.user.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.encrypt.AesEncryptUtils;
import pers.liujunyi.cloud.common.encrypt.autoconfigure.EncryptProperties;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.security.domain.user.UserAccountsDto;
import pers.liujunyi.cloud.security.domain.user.UserAccountsUpdateDto;
import pers.liujunyi.cloud.security.entity.user.UserAccounts;
import pers.liujunyi.cloud.security.repository.elasticsearch.user.UserAccountsElasticsearchRepository;
import pers.liujunyi.cloud.security.repository.jpa.user.UserAccountsRepository;
import pers.liujunyi.cloud.security.service.user.UserAccountsService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: UserAccountsServiceImpl.java
 * 文件描述: 用户帐号信息 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class UserAccountsServiceImpl extends BaseServiceImpl<UserAccounts, Long> implements UserAccountsService {

    @Autowired
    private UserAccountsRepository userAccountsRepository;
    @Autowired
    private UserAccountsElasticsearchRepository userAccountsElasticsearchRepository;
    @Autowired
    private EncryptProperties encryptProperties;


    public UserAccountsServiceImpl(BaseRepository<UserAccounts, Long> baseRepository) {
        super(baseRepository);
    }


    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultInfo saveRecord(UserAccountsDto record) {
        String result = this.saveUserAccountsRecord(record);
        switch (result) {
            case "-10":
                return ResultUtil.params("帐号重复,请重新输入");
            case "-20":
                return ResultUtil.params("用户编号重复,请重新输入");
            case "-30":
                return ResultUtil.params("你绑定的手机号已被使用,请重新输入");
            case "0":
                return ResultUtil.fail();
            default:
                break;
        }
        return ResultUtil.success(result);
    }


    @Override
    public String saveUserAccountsRecord(UserAccountsDto record) {
        if (this.checkUserAccountsRepetition(record.getUserAccounts(), record.getId())) {
            return "-10";
        }
        if (this.checkUserNumberRepetition(record.getUserNumber(), record.getId())) {
            return "-20";
        }
        if (StringUtils.isNotBlank(record.getMobilePhone())) {
            if (this.checkMobilePhoneRepetition(record.getMobilePhone(), record.getId())) {
                return "-30";
            }
        }
        if (record.getId() ==  null) {
            record.setRegistrationTime(new Date());
        }
        if (record.getUserStatus() == null) {
            record.setUserStatus(SecurityConstant.ENABLE_STATUS);
        }
        if (StringUtils.isNotBlank(record.getUserNickName())) {
            record.setUserNickName(record.getUserName());
        }
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // 解密前端传入的加密参数
        String curUserPassWord = AesEncryptUtils.aesDecrypt(record.getUserPassword(), encryptProperties.getSecretKey());
        record.setUserPassword(passwordEncoder.encode(curUserPassWord));
        UserAccounts userAccounts = DozerBeanMapperUtil.copyProperties(record, UserAccounts.class);
        UserAccounts saveObject = this.userAccountsRepository.save(userAccounts);
        if (saveObject == null || saveObject.getId() == null) {
            return "0";
        }
        this.userAccountsElasticsearchRepository.save(saveObject);
        return String.valueOf(saveObject.getId());
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids, String putParams) {
        boolean success = this.updateUserAccountsStatus(status, ids, putParams);
        if (success) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo updateStatus(Byte status, Long id, Long dataVersion) {
        boolean success = this.updateAccountsStatus(status, id, dataVersion);
        if (success) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public Boolean updateAccountsStatus(Byte status, Long id, Long dataVersion) {
        int count = this.userAccountsRepository.setUserStatusById(status, new Date(), id, dataVersion);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("userStatus", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            docDataMap.put("dataVersion", dataVersion + 1);
            sourceMap.put(String.valueOf(id), docDataMap);
            // 更新 Elasticsearch 中的数据
            super.updateBatchElasticsearchData(sourceMap);
            return true;
        }
        return false;    }

    @Override
    public Boolean updateUserAccountsStatus(Byte status, List<Long> ids, String putParams) {
        int count = this.userAccountsRepository.setUserStatusByIds(status, new Date(), ids);
        if (count > 0) {
            JSONArray jsonArray = JSONArray.parseArray(putParams);
            int jsonSize = jsonArray.size();
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            for(int i = 0; i < jsonSize; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, Object> docDataMap = new HashMap<>();
                docDataMap.put("userStatus", status);
                docDataMap.put("updateTime", System.currentTimeMillis());
                docDataMap.put("dataVersion", jsonObject.getLongValue("dataVersion") + 1);
                sourceMap.put(jsonObject.getString("id"), docDataMap);
            }
            super.updateBatchElasticsearchData(sourceMap);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultInfo updateUserPassWord(Long id, String historyPassWord, String currentPassWord, Long dataVersion) {
        UserAccounts userAccounts = this.getUserAccounts(id);
        if (userAccounts != null) {
            //检查历史密码是否正确
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            boolean identical = historyPassWord.equals("忽略") ? true : passwordEncoder.matches(historyPassWord, userAccounts.getUserPassword());
            if (!identical) {
                return ResultUtil.params("原始密码错误");
            }
            int count = this.userAccountsRepository.setUserPasswordById(currentPassWord, new Date(), id, dataVersion);
            if (count > 0) {
                Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
                Map<String, Object> docDataMap = new HashMap<>();
                docDataMap.put("userPassword", currentPassWord);
                docDataMap.put("changePasswordTime", System.currentTimeMillis());
                docDataMap.put("updateTime", System.currentTimeMillis());
                docDataMap.put("dataVersion", dataVersion + 1);
                sourceMap.put(String.valueOf(id), docDataMap);
                super.updateBatchElasticsearchData(sourceMap);
                return ResultUtil.success();
            }
        }
        return ResultUtil.fail();
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultInfo updateUserAccountsInfo(UserAccountsUpdateDto userAccountsUpdate) {
        Long id = userAccountsUpdate.getId();
        String userAccounts = userAccountsUpdate.getUserAccounts();
        String userNumber = userAccountsUpdate.getUserNumber();
        String mobilePhone = userAccountsUpdate.getMobilePhone();
        String userMailbox = userAccountsUpdate.getUserMailbox();
        String userName = userAccountsUpdate.getUserName();
        String userNickName = userAccountsUpdate.getUserNickName();
        UserAccounts accounts = this.getUserAccounts(id);
        Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
        Map<String, Object> docDataMap = new HashMap<>();
        if (StringUtils.isNotBlank(userAccounts) ) {
            if ( this.checkUserAccountsRepetition(userAccounts, id)) {
                return ResultUtil.params("帐号重复,请重新输入");
            }
            accounts.setUserAccounts(userAccounts);
            docDataMap.put("userAccounts", userAccounts);
        }
        if (StringUtils.isNotBlank(userNumber) ) {
            if ( this.checkUserNumberRepetition(userNumber, id)) {
                return ResultUtil.params("用户编号重复,请重新输入");
            }
            accounts.setUserNumber(userNumber);
            docDataMap.put("userNumber", userNumber);
        }
        if (StringUtils.isNotBlank(mobilePhone) ) {
            if ( this.checkMobilePhoneRepetition(mobilePhone, id)) {
                return ResultUtil.params("你绑定的手机号已被使用,请重新输入");
            }
            accounts.setMobilePhone(mobilePhone);
            docDataMap.put("mobilePhone", mobilePhone);
        }
        if (StringUtils.isNotBlank(userMailbox)) {
            accounts.setUserMailbox(userMailbox);
            docDataMap.put("userMailbox", userMailbox);
        }
        if (StringUtils.isNotBlank(userName)) {
            accounts.setUserName(userName);
            docDataMap.put("userName", userName);
        }
        if (StringUtils.isNotBlank(userNickName)) {
            accounts.setUserNickName(userNickName);
            docDataMap.put("userNickName", userNickName);
        }
        accounts.setUpdateTime(new Date());
        UserAccounts saveObj = this.userAccountsRepository.save(accounts);
        if (saveObj != null) {
            docDataMap.put("updateTime", System.currentTimeMillis());
            docDataMap.put("dataVersion", userAccountsUpdate.getDataVersion() + 1);
            sourceMap.put(String.valueOf(id), docDataMap);
            // 更新 Elasticsearch 中的数据
            super.updateBatchElasticsearchData(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        boolean success = this.deleteByUserAccounts(ids);
        if (success) {
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Transactional(rollbackFor = {RuntimeException.class, Exception.class})
    @Override
    public ResultInfo deleteSingle(Long id) {
        this.userAccountsRepository.deleteById(id);
        this.userAccountsElasticsearchRepository.deleteById(id);
        return ResultUtil.success();
    }


    @Override
    public Boolean deleteByUserAccounts(List<Long> ids) {
        long count = this.userAccountsRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.userAccountsElasticsearchRepository.deleteByIdIn(ids);
            return true;
        }
        return false;
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        this.userAccountsSyncDataToElasticsearch();
        return ResultUtil.success();
    }

    @Override
    public void userAccountsSyncDataToElasticsearch() {
        Sort sort =  new Sort(Sort.Direction.ASC, "id");
        List<UserAccounts> list = this.userAccountsRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(list)) {
            this.userAccountsElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = list.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<UserAccounts> partList = new LinkedList<>(list.subList(0, pointsDataLimit));
                    //剔除
                    list.subList(0, pointsDataLimit).clear();
                    this.userAccountsElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(list)) {
                    this.userAccountsElasticsearchRepository.saveAll(list);
                }
            } else {
                this.userAccountsElasticsearchRepository.saveAll(list);
            }
        } else {
            this.userAccountsElasticsearchRepository.deleteAll();
        }
    }

    /**
     * 根据id 获取数据
     * @param id
     * @return
     */
    private UserAccounts getUserAccounts(Long id) {
        Optional<UserAccounts> userAccounts = this.userAccountsElasticsearchRepository.findById(id);
        if (userAccounts.isPresent()) {
            return userAccounts.get();
        }
        return null;
    }

    /**
     * 检测机用户编号是否重复
     * @param userNumber
     * @return 重复返回 true   不重复返回  false
     */
    private Boolean checkUserNumberRepetition(String userNumber, Long id) {
        if (id == null){
            return this.checkUserNumberData(userNumber);
        } else {
            UserAccounts userAccounts = this.getUserAccounts(id);
            if (userAccounts != null && !userAccounts.getUserNumber().equals(userNumber)) {
                return this.checkUserNumberData(userNumber);
            }
        }
        return false;
    }

    /**
     * 检查库中是否存在userNumber数据
     * @param userNumber
     * @return
     */
    private Boolean checkUserNumberData(String userNumber) {
        UserAccounts userAccounts = this.userAccountsElasticsearchRepository.findFirstByUserNumber(userNumber);
        if (userAccounts != null) {
            return true;
        }
        return false;
    }


    /**
     * 检测机用户账户是否重复
     * @param userAccounts
     * @return 重复返回 true   不重复返回  false
     */
    private Boolean checkUserAccountsRepetition(String userAccounts, Long id) {
        if (id == null){
            return this.checkUserAccountsData(userAccounts);
        } else {
            UserAccounts accounts = this.getUserAccounts(id);
            if (accounts != null && !accounts.getUserAccounts().equals(userAccounts)) {
                return this.checkUserAccountsData(userAccounts);
            }
        }
        return false;
    }

    /**
     * 检查库中是否存在userNumber数据
     * @param userNumber
     * @return
     */
    private Boolean checkUserAccountsData(String userNumber) {
        UserAccounts userAccounts = this.userAccountsElasticsearchRepository.findFirstByUserAccounts(userNumber);
        if (userAccounts != null) {
            return true;
        }
        return false;
    }


    /**
     * 检测机用户电话是否重复
     * @param mobilePhone
     * @return 重复返回 true   不重复返回  false
     */
    private Boolean checkMobilePhoneRepetition(String mobilePhone, Long id) {
        if (id == null){
            return this.checkMobilePhoneData(mobilePhone);
        } else {
            UserAccounts userAccounts = this.getUserAccounts(id);
            if (userAccounts != null && !userAccounts.getMobilePhone().equals(mobilePhone)) {
                return this.checkMobilePhoneData(mobilePhone);
            }
        }
        return false;
    }

    /**
     * 检查库中是否存在userNumber数据
     * @param mobilePhone
     * @return
     */
    private Boolean checkMobilePhoneData(String mobilePhone) {
        UserAccounts userAccounts = this.userAccountsElasticsearchRepository.findFirstByMobilePhone(mobilePhone);
        if (userAccounts != null) {
            return true;
        }
        return false;
    }
}
