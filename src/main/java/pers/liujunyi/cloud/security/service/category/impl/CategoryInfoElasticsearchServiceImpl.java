package pers.liujunyi.cloud.security.service.category.impl;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.repository.elasticsearch.BaseElasticsearchRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseElasticsearchServiceImpl;
import pers.liujunyi.cloud.security.domain.category.CategoryInfoQueryDto;
import pers.liujunyi.cloud.security.entity.category.CategoryInfo;
import pers.liujunyi.cloud.security.repository.elasticsearch.category.CategoryInfoElasticsearchRepository;
import pers.liujunyi.cloud.security.service.category.CategoryInfoElasticsearchService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/***
 * 文件名称: CategoryInfoElasticsearchServiceImpl.java
 * 文件描述: 流程分类 Elasticsearch Service impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class CategoryInfoElasticsearchServiceImpl extends BaseElasticsearchServiceImpl<CategoryInfo, Long> implements CategoryInfoElasticsearchService {

    @Autowired
    private CategoryInfoElasticsearchRepository categoryInfoElasticsearchRepository;


    public CategoryInfoElasticsearchServiceImpl(BaseElasticsearchRepository<CategoryInfo, Long> baseElasticsearchRepository) {
        super(baseElasticsearchRepository);
    }


    @Override
    public ResultInfo findPageGird(CategoryInfoQueryDto query) {
        // 排序方式 解决无数据时异常 No mapping found for [createTime] in order to sort on
        SortBuilder sortBuilder = SortBuilders.fieldSort("createTime").unmappedType("date").order(SortOrder.DESC);
        // 如果使用这种排序方式 如果表中数据为空时,会报异常 No mapping found for [createTime] in order to sort on
        //Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        // 查询数据
        SearchQuery searchQuery = query.toSpecSortPageable(sortBuilder);
        Page<CategoryInfo> searchPageResults = this.categoryInfoElasticsearchRepository.search(searchQuery);
        List<CategoryInfo> searchDataList = searchPageResults.getContent();
        Long totalElements =  searchPageResults.getTotalElements();
        ResultInfo result = ResultUtil.success(searchDataList);
        result.setTotal(totalElements);
        return  result;
    }



    @Override
    public CategoryInfo findById(Long id) {
        Optional<CategoryInfo> optional  = this.categoryInfoElasticsearchRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }


    @Override
    public List<Map<String, String>> categorySelect(CategoryInfoQueryDto query) {
        query.setCategoryStatus(SecurityConstant.ENABLE_STATUS);
        List<Map<String, String>> result = new LinkedList<>();
        //分页参数
        Pageable pageable = this.allPageable;
        // 查询数据
        SearchQuery searchQuery = query.toSpecPageable(pageable);
        Page<CategoryInfo> searchPageResults = this.categoryInfoElasticsearchRepository.search(searchQuery);
        List<CategoryInfo> CategoryInfoList = searchPageResults.getContent();
        if (!CollectionUtils.isEmpty(CategoryInfoList)) {
            CategoryInfoList.stream().forEach(item -> {
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
            List<CategoryInfo> categoryList = this.categoryInfoElasticsearchRepository.findByCategoryTypeAndCategoryName(categoryType, categoryName);
            if (!CollectionUtils.isEmpty(categoryList)) {
                result = "false";
            }
        }
        return result;
    }

}
