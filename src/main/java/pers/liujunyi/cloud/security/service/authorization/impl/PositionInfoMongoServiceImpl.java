package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseMongoServiceImpl;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.authorization.PositionInfoQueryDto;
import pers.liujunyi.cloud.security.entity.authorization.PositionInfo;
import pers.liujunyi.cloud.security.repository.mongo.authorization.MenuResourceMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.PositionInfoMongoService;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: PositionInfoMongoServiceImpl.java
 * 文件描述: 岗位 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class PositionInfoMongoServiceImpl extends BaseMongoServiceImpl<PositionInfo, Long> implements PositionInfoMongoService {

    @Autowired
    private MenuResourceMongoRepository menuResourceMongoRepository;

    public PositionInfoMongoServiceImpl(BaseMongoRepository<PositionInfo, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }

    @Override
    public List<ZtreeNode> positionTree(Long pid, Byte status) {
        return null;
    }

    @Override
    public List<ZtreeNode> positionFullParentCodeTree(String fullParentCode) {
        return null;
    }

    @Override
    public ResultInfo findPageGird(PositionInfoQueryDto query) {
        return null;
    }

    @Override
    public String getPositionName(Long id) {
        return null;
    }

    @Override
    public Map<Long, String> findKeyIdValueNameByIdIn(List<Long> ids) {
        return null;
    }

    @Override
    public ResultInfo selectById(Long id) {
        return null;
    }

    @Override
    public Map<Long, String> positionNameToMap(List<Long> ids) {
        return null;
    }
}
