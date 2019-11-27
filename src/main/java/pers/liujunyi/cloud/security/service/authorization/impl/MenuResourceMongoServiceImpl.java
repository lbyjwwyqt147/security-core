package pers.liujunyi.cloud.security.service.authorization.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.service.impl.BaseMongoServiceImpl;
import pers.liujunyi.cloud.common.vo.tree.ZtreeNode;
import pers.liujunyi.cloud.security.domain.authorization.MenuResourceQueryDto;
import pers.liujunyi.cloud.security.entity.authorization.MenuResource;
import pers.liujunyi.cloud.security.repository.mongo.authorization.MenuResourceMongoRepository;
import pers.liujunyi.cloud.security.service.authorization.MenuResourceMongoService;

import java.util.List;
import java.util.Map;

/***
 * 文件名称: MenuResourceMongoServiceImpl.java
 * 文件描述: 菜单资源 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年03月10日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class MenuResourceMongoServiceImpl extends BaseMongoServiceImpl<MenuResource, Long>  implements MenuResourceMongoService {

    @Autowired
    private MenuResourceMongoRepository menuResourceMongoRepository;

    public MenuResourceMongoServiceImpl(BaseMongoRepository<MenuResource, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }

    @Override
    public List<ZtreeNode> menuResourceTree(Long pid, Byte status) {
        return null;
    }

    @Override
    public List<ZtreeNode> menuResourceFullParentCodeTree(String fullParentCode) {
        return null;
    }

    @Override
    public ResultInfo findPageGird(MenuResourceQueryDto query) {
        return null;
    }

    @Override
    public String getMenuResourceName(Long id) {
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
    public Map<Long, String> menuResourceNameToMap(List<Long> ids) {
        return null;
    }
}
