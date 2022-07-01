package io.dataease.service.publish.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dataease.dto.publish.PublishApiDTO;
import io.dataease.plugins.common.base.domain.PublishApi;
import io.dataease.plugins.common.base.mapper.DatasetTableMapper;
import io.dataease.plugins.common.base.mapper.PublishApiMapper;
import io.dataease.service.publish.PublishApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dever
 * @description 针对表【z_t_publish_api(自定义发布数据API表)】的数据库操作Service实现
 * @createDate 2022-06-29 13:06:07
 */
@Service
public class PublishApiServiceImpl extends ServiceImpl<PublishApiMapper, PublishApi>
        implements PublishApiService {

    @Autowired
    private DatasetTableMapper datasetTableMapper;

    @Override
    public Map<String, Object> listRecords(Long pageSize, Long index) {
        IPage<PublishApi> publishApiPage = page(new Page<PublishApi>()
                        .setPages(pageSize)
                        .setCurrent(index));
        List<PublishApi> list = publishApiPage.getRecords();
        List<PublishApiDTO> rsList = list.stream().map(l->{
            PublishApiDTO publishApiDTO = new PublishApiDTO();
            BeanUtil.copyProperties(l, publishApiDTO, false);
            publishApiDTO.setDatasetName(datasetTableMapper.selectByPrimaryKey(l.getDatasetId()).getName());
            return publishApiDTO;
        }).collect(Collectors.toList());
        return Map.of("records", rsList,
                "total", publishApiPage.getTotal());
    }
}
