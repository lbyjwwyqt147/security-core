package pers.liujunyi.cloud.security.service.organizations.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.security.entity.organizations.StaffOrg;
import pers.liujunyi.cloud.security.repository.elasticsearch.organizations.StaffOrgElasticsearchRepository;
import pers.liujunyi.cloud.security.repository.jpa.organizations.StaffOrgRepository;
import pers.liujunyi.cloud.security.service.organizations.StaffOrgService;

import java.util.List;

/***
 * 文件名称: StaffOrgServiceImpl.java
 * 文件描述: 职工关联组织机构 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class StaffOrgServiceImpl extends BaseServiceImpl<StaffOrg, Long> implements StaffOrgService {

    @Autowired
    private StaffOrgRepository staffOrgRepository;
    @Autowired
    private StaffOrgElasticsearchRepository staffOrgElasticsearchRepository;


    public StaffOrgServiceImpl(BaseRepository<StaffOrg, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(StaffOrg record) {
      return null;
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        return null;
    }

    @Override
    public ResultInfo batchDeletes(List<Long> ids) {
        return null;
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        return null;
    }
}
