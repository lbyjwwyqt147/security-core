package pers.liujunyi.cloud.security.service.authorization.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.security.domain.authorization.MenuResourceDto;
import pers.liujunyi.cloud.security.entity.authorization.MenuResource;
import pers.liujunyi.cloud.security.entity.authorization.RoleResource;
import pers.liujunyi.cloud.security.repository.jpa.authorization.MenuResourceRepository;
import pers.liujunyi.cloud.security.repository.mongo.authorization.RoleResourceMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.MenuResourceMongoService;
import pers.liujunyi.cloud.security.service.authorization.MenuResourceService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: MenuResourceServiceImpl.java
 * 文件描述: 角色资源 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class MenuResourceServiceImpl extends BaseServiceImpl<MenuResource, Long> implements MenuResourceService {

    @Autowired
    private MenuResourceRepository menuResourceRepository;
    @Autowired
    private MenuResourceMongoService menuResourceMongoService;
    @Autowired
    private RoleResourceMongoRepository roleResourceMongoRepository;

    public MenuResourceServiceImpl(BaseRepository<MenuResource, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public ResultInfo saveRecord(MenuResourceDto record) {
        boolean add = record.getId() == null ? true : false;
        if (this.checkMenuNumberRepetition(record.getMenuNumber(), record.getId())) {
            return ResultUtil.params("资源编号重复,请重新输入！");
        }
        MenuResource menuResource = DozerBeanMapperUtil.copyProperties(record, MenuResource.class);
        if (record.getMenuStatus() == null) {
            menuResource.setMenuStatus(SecurityConstant.ENABLE_STATUS);
        }
        if (record.getParentId().longValue() > 0) {
            MenuResource parent = this.findById(record.getParentId());
            menuResource.setFullMenuParent(parent.getFullMenuParent() + ":"  + parent.getId());
            menuResource.setMenuLevel((byte)(parent.getMenuLevel() + 1));
            String curFullParentCode = StringUtils.isNotBlank(parent.getFullMenuParentCode()) && !parent.getFullMenuParentCode().equals("0") ? parent.getFullMenuParentCode() + ":" + record.getMenuNumber() : parent.getMenuNumber();
            menuResource.setFullMenuParentCode(curFullParentCode);
        } else {
            menuResource.setFullMenuParent("0");
            menuResource.setMenuLevel((byte) 1);
        }
        MenuResource saveObject = this.menuResourceRepository.save(menuResource);
        if (saveObject == null || saveObject.getId() == null) {
            return ResultUtil.fail();
        }
        if (!add) {
            saveObject.setDataVersion(saveObject.getDataVersion() + 1);
        }
        this.menuResourceMongoService.save(saveObject);
        return ResultUtil.success(saveObject.getId());
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        if (status.byteValue() == 1) {
            ResultInfo resultInfo = this.checkUseCondition(ids, "禁用");
            if (!resultInfo.getSuccess()) {
                return resultInfo;
            }
        }
        int count = this.menuResourceRepository.setStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>(ids.size());
            ids.stream().forEach(item -> {
                Map<String, Object> docDataMap = new HashMap<>(2);
                docDataMap.put("menuStatus", status);
                docDataMap.put("updateTime", System.currentTimeMillis());
                sourceMap.put(item.toString(), docDataMap);
            });
            // 更新 Mongo 中的数据
            super.updateMongoDataByIds(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        ResultInfo resultInfo = this.checkUseCondition(ids, "删除");
        if (!resultInfo.getSuccess()) {
            return resultInfo;
        }
        long count = this.menuResourceRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.menuResourceMongoService.deleteAllByIdIn(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo syncDataToMongo() {
        super.syncDataMongoDb();
        return ResultUtil.success();
    }

    /**
     * 检测资源编号是否重复
     * @param menuNumber
     * @return 重复返回 true   不重复返回  false
     */
    private Boolean checkMenuNumberRepetition(String menuNumber, Long id) {
        if (id == null){
            return this.checkMenuNumberData(menuNumber);
        } else {
            MenuResource menuResource = this.findById(id);
            if (menuResource != null && !menuResource.getMenuNumber().equals(menuNumber)) {
                return this.checkMenuNumberData(menuNumber);
            }
        }
        return false;
    }

    /**
     * 检查库中是否存在menuNumber 数据
     * @param menuNumber
     * @return
     */
    private Boolean checkMenuNumberData (String menuNumber) {
        MenuResource menuResource = this.menuResourceMongoService.findFirstByMenuNumber(menuNumber);
        if (menuResource != null) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param id
     * @return
     */
    private MenuResource findById(Long id) {
        MenuResource menuResource = this.menuResourceMongoService.findById(id);
        return menuResource;
    }

    /**
     * 检测数据是否被系统使用
     * @param ids
     * @return
     */
    private ResultInfo checkUseCondition(List<Long> ids, String title) {
        List<RoleResource> list = this.roleResourceMongoRepository.findByResourceIdInAndStatus(ids, null);
        if (!CollectionUtils.isEmpty(list)) {
            return ResultUtil.params("要"+title+"的资源正在被系统使用,不能被"+title+"");
        }
        return ResultUtil.success();
    }

}
