package io.dataease.service.publish;

import com.baomidou.mybatisplus.extension.service.IService;
import io.dataease.plugins.common.base.domain.PublishApi;

import java.util.Map;

/**
* @author dever
* @description 针对表【z_t_publish_api(自定义发布数据API表)】的数据库操作Service
* @createDate 2022-06-29 13:06:07
*/
public interface PublishApiService extends IService<PublishApi> {

    Map<String, Object> listRecords(Long pageSize, Long index);

}
