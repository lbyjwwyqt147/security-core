package pers.liujunyi.cloud.security.service.category.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pers.liujunyi.cloud.common.exception.ErrorCodeEnum;
import pers.liujunyi.cloud.common.repository.jpa.BaseRepository;
import pers.liujunyi.cloud.common.restful.ResultInfo;
import pers.liujunyi.cloud.common.restful.ResultUtil;
import pers.liujunyi.cloud.common.service.impl.BaseServiceImpl;
import pers.liujunyi.cloud.common.util.DozerBeanMapperUtil;
import pers.liujunyi.cloud.common.util.UserContext;
import pers.liujunyi.cloud.security.domain.category.CategoryInfoDto;
import pers.liujunyi.cloud.security.entity.category.CategoryInfo;
import pers.liujunyi.cloud.security.repository.elasticsearch.category.CategoryInfoElasticsearchRepository;
import pers.liujunyi.cloud.security.repository.jpa.category.CategoryInfoRepository;
import pers.liujunyi.cloud.security.service.category.CategoryInfoService;
import pers.liujunyi.cloud.security.util.SecurityConstant;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/***
 * 文件名称: CategoryInfoServiceImpl.java
 * 文件描述: 分类信息 Service Impl
 * 公 司:
 * 内容摘要:
 * 其他说明:
 * 完成日期:2019年11月07日
 * 修改记录:
 * @version 1.0
 * @author ljy
 */
@Service
public class CategoryInfoServiceImpl extends BaseServiceImpl<CategoryInfo, Long> implements CategoryInfoService {

    @Autowired
    private CategoryInfoRepository categoryInfoRepository;
    @Autowired
    private CategoryInfoElasticsearchRepository categoryInfoElasticsearchRepository;

    public CategoryInfoServiceImpl(BaseRepository<CategoryInfo, Long> baseRepository) {
        super(baseRepository);
    }


    @Override
    public ResultInfo saveRecord(CategoryInfoDto record) {
        ResultInfo result = ResultUtil.success();
        if (record.getId() != null) {
            record.setUpdateTime(new Date());
            record.setUpdateUserId(UserContext.currentUserId());
        }
        if (record.getCategoryStatus() == null) {
            record.setCategoryStatus(SecurityConstant.ENABLE_STATUS);
        }
        CategoryInfo categoryInfo = DozerBeanMapperUtil.copyProperties(record, CategoryInfo.class);
        CategoryInfo saveObj =  this.categoryInfoRepository.save(categoryInfo);
        if (saveObj != null && saveObj.getId() != null) {
            this.categoryInfoElasticsearchRepository.save(saveObj);
        }else {
            result.setSuccess(false);
            result.setStatus(ErrorCodeEnum.FAIL.getCode());
        }
        return result;
    }

    @Override
    public ResultInfo updateStatus(Byte status, List<Long> ids) {
        int count = this.categoryInfoRepository.setStatusByIds(status, new Date(), ids);
        if (count > 0) {
            Map<String, Map<String, Object>> sourceMap = new ConcurrentHashMap<>();
            Map<String, Object> docDataMap = new HashMap<>();
            docDataMap.put("categoryStatus", status);
            docDataMap.put("updateTime", System.currentTimeMillis());
            ids.stream().forEach(item -> {
                sourceMap.put(String.valueOf(item), docDataMap);
            });
            super.updateBatchElasticsearchData(sourceMap);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo deleteBatch(List<Long> ids) {
        long count = this.categoryInfoRepository.deleteByIdIn(ids);
        if (count > 0) {
            this.categoryInfoElasticsearchRepository.deleteByIdIn(ids);
            return ResultUtil.success();
        }
        return ResultUtil.fail();
    }

    @Override
    public ResultInfo syncDataToElasticsearch() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<CategoryInfo> list = this.categoryInfoRepository.findAll(sort);
        if (!CollectionUtils.isEmpty(list)) {
            this.categoryInfoElasticsearchRepository.deleteAll();
            // 限制条数
            int pointsDataLimit = 1000;
            int size = list.size();
            //判断是否有必要分批
            if(pointsDataLimit < size){
                //分批数
                int part = size/pointsDataLimit;
                for (int i = 0; i < part; i++) {
                    //1000条
                    List<CategoryInfo> partList = new LinkedList<>(list.subList(0, pointsDataLimit));
                    //剔除
                    list.subList(0, pointsDataLimit).clear();
                    this.categoryInfoElasticsearchRepository.saveAll(partList);
                }
                //表示最后剩下的数据
                if (!CollectionUtils.isEmpty(list)) {
                    this.categoryInfoElasticsearchRepository.saveAll(list);
                }
            } else {
                this.categoryInfoElasticsearchRepository.saveAll(list);
            }
        } else {
            this.categoryInfoElasticsearchRepository.deleteAll();
        }
        return ResultUtil.success();
    }

}
