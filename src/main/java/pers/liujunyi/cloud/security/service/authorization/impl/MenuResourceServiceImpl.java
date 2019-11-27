package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.security.domain.authorization.MenuResourceDto;
import pers.liujunyi.cloud.security.entity.authorization.MenuResource;
import pers.liujunyi.cloud.security.repository.jpa.authorization.MenuResourceRepository;
import pers.liujunyi.cloud.security.service.authorization.MenuResourceService;

import java.util.List;

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

    public MenuResourceServiceImpl(BaseRepository<MenuResource, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public ResultInfo saveRecord(MenuResourceDto record) {
        return null;
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        return null;
    }

    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        return null;
    }

    @Override
    public ResultInfo syncDataToMongo() {
        return null;
    }
}
