package pers.liujunyi.cloud.security.repository.mongo.authorization;

import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.security.entity.authorization.MenuResource;

import java.util.List;

/***
 * 文件名称: MenuResourceMongoRepository.java
 * 文件描述: 菜单资源 Mongo Repository
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
public interface MenuResourceMongoRepository extends BaseMongoRepository<MenuResource, Long> {

    /**
     * 根据 编号 获取数据
     * @param menuNumber 编号
     * @return
     */
    MenuResource findFirstByMenuNumber(String menuNumber);

    /**
     * 根据pid 获取数据
     * @param pid
     * @param menuStatus  0：正常  1：禁用
     * @return
     */
    List<MenuResource> findByParentIdAndMenuStatusOrderBySerialNumberAsc(Long pid, Byte menuStatus);

    /**
     * 根据menuStatus获取数据
     * @param menuStatus  0：正常  1：禁用
     * @return
     */
    List<MenuResource> findByMenuStatusOrderBySerialNumberAsc(Byte menuStatus);

    /**
     * 根据ids menuStatus获取数据
     * @param ids
     * @param menuClassify  资源类型
     * @param menuStatus  0：正常  1：禁用
     * @return
     */
    List<MenuResource> findByIdInAndMenuClassifyInAndMenuStatusOrderBySerialNumberAsc(List<Long> ids, List<Byte> menuClassify, Byte menuStatus);

    /**
     * 根据menuClassify menuStatus获取数据
     * @param menuClassify  资源类型
     * @param menuStatus  0：正常  1：禁用
     * @return
     */
    List<MenuResource> findByMenuClassifyInAndMenuStatusAndMenuPathIsNotNull(Byte menuClassify, Byte menuStatus);

    /**
     * 根据pid 获取数据
     * @param pid
     * @return
     */
    List<MenuResource> findByParentIdOrderBySerialNumberAsc(Long pid);

    /**
     * 根据pid  menuNumber 获取数据
     * @param pid
     * @param menuNumber
     * @return
     */
    List<MenuResource> findByParentIdAndMenuNumber(Long pid, String menuNumber);

    /**
     * 根据 fullMenuParentCode 获取数据
     * @param fullMenuParentCode
     * @param menuStatus  0：正常  1：禁用
     * @return
     */
    List<MenuResource> findByFullMenuParentCodeLikeAndMenuStatusOrderBySerialNumberAsc(String fullMenuParentCode, Byte menuStatus);

    /**
     * 根据pid  menuStatus 获取数据
     * @param pid
     * @param menuStatus
     * @return
     */
    List<MenuResource> findByParentIdInAndMenuStatusOrderBySerialNumberAsc(List<Long> pid, Byte menuStatus);

}
