package pers.liujunyi.cloud.security.service.category.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.mongo.BaseMongoRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseMongoServiceImpl;
import pers.liujunyi.cloud.security.domain.category.CategoryInfoQueryDto;
import pers.liujunyi.cloud.security.entity.category.CategoryInfo;
import pers.liujunyi.cloud.security.repository.mongo.category.CategoryInfoMongoRepository;
import pers.liujunyi.cloud.security.service.category.CategoryInfoMongoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/***
 * 文件名称: CategoryInfoMongoServiceImpl.java
 * 文件描述: 流程分类 Mongo Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class CategoryInfoMongoServiceImpl extends BaseMongoServiceImpl<CategoryInfo, Long> implements CategoryInfoMongoService {

    @Autowired
    private CategoryInfoMongoRepository categoryInfoMongoRepository;


    public CategoryInfoMongoServiceImpl(BaseMongoRepository<CategoryInfo, Long> baseMongoRepository) {
        super(baseMongoRepository);
    }


    @Override
    public ResultInfo findPageGird(CategoryInfoQueryDto query) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        Pageable pageable = query.toPageable(sort);
        // 查询条件
        Query searchQuery = query.toSpecPageable(pageable);
        // 查询总记录条数
        long totalElements = this.mongoDbTemplate.count(searchQuery, CategoryInfo.class);
        // 查询数据
        List<CategoryInfo> searchPageResults =  this.mongoDbTemplate.find(searchQuery, CategoryInfo.class);
        ResultInfo result = ResultUtil.success(searchPageResults);
        result.setTotal(totalElements);
        return  result;
    }



    @Override
    public CategoryInfo findById(Long id) {
        Optional<CategoryInfo> optional  = this.categoryInfoMongoRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }


    @Override
    public List<Map<String, String>> categorySelect(CategoryInfoQueryDto query) {
        query.setCategoryStatus(SecurityConstant.ENABLE_STATUS);
        List<Map<String, String>> result = new LinkedList<>();
        // 查询条件
        Query searchQuery = query.toSpecPageable(null);
        // 查询数据
        List<CategoryInfo> searchPageResults =  this.mongoDbTemplate.find(searchQuery, CategoryInfo.class);
        if (!CollectionUtils.isEmpty(searchPageResults)) {
            searchPageResults.stream().forEach(item -> {
                Map<String, String> map = new ConcurrentHashMap<>();
                map.put("id", item.getId().toString());
                map.put("text", item.getCategoryName());
                result.add(map);
            });
        }
        return result;
    }

    @Override
    public Map<Long, String> getCategoryNameMap(List<Long> ids) {
        List<CategoryInfo> CategoryInfoList = this.findAllByIdIn(ids);
        if (!CollectionUtils.isEmpty(CategoryInfoList)) {
            Map<Long, String> nameMap = new ConcurrentHashMap<>();
            CategoryInfoList.stream().forEach(item -> {
                nameMap.put(item.getId(), item.getCategoryName());
            });
            return nameMap;
        }
        return null;
    }

    @Override
    public String verifyCategoryName(Byte categoryType, String categoryName, String history) {
        String result = "true";
        boolean verify = true;
        if (StringUtils.isNotBlank(history) && categoryName.equals(history)) {
            verify = false;
        }
        if (verify) {
            List<CategoryInfo> categoryList = this.categoryInfoMongoRepository.findByCategoryTypeAndCategoryName(categoryType, categoryName);
            if (!CollectionUtils.isEmpty(categoryList)) {
                result = "false";
            }
        }
        return result;
    }

}
