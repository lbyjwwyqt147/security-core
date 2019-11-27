package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.security.domain.authorization.PositionInfoDto;
import pers.liujunyi.cloud.security.entity.authorization.PositionInfo;
import pers.liujunyi.cloud.security.repository.jpa.authorization.PositionInfoRepository;
import pers.liujunyi.cloud.security.service.authorization.PositionInfoService;

import java.util.List;

/***
 * 文件名称: PositionInfoServiceImpl.java
 * 文件描述: 岗位信息 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class PositionInfoServiceImpl extends BaseServiceImpl<PositionInfo, Long> implements PositionInfoService {

    @Autowired
    private PositionInfoRepository positionInfoRepository;

    public PositionInfoServiceImpl(BaseRepository<PositionInfo, Long> baseRepository) {
        super(baseRepository);
    }

    @Override
    public ResultInfo saveRecord(PositionInfoDto record) {
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
