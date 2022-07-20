package io.dataease.service.publish;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dataease.plugins.common.base.domain.PublishApi;

import java.util.List;
import java.util.Map;

/**
* @author dever
* @description 针对表【z_t_publish_api(自定义发布数据API表)】的数据库操作Service
* @createDate 2022-06-29 13:06:07
*/
public interface PublishApiService extends IService<PublishApi> {

    /**
     * 列出表数据
     * @param pageSize
     * @param index
     * @return
     */
    Map<String, Object> listRecords(Long pageSize, Long index);

    /**
     * 根据发布ID获取发布DataSet的值
     * @param prefixUri 路径前缀
     * @param publishId 发布ID
     * @return
     */
    List<Map<String, Object>> queryDataSetData(String prefixUri, Integer publishId) throws Exception;

}
