package io.dataease.controller.publish;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.dataease.controller.request.dataset.DataSetTableRequest;
import io.dataease.dto.dataset.DataSetTableDTO;
import io.dataease.plugins.common.base.domain.PublishApi;
import io.dataease.service.dataset.DataSetTableService;
import io.dataease.service.publish.PublishApiService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Api(tags = "数据发布：发布API接口")
@ApiSupport(order = 99)
@RestController
@RequestMapping("/publish/api")
public class PublishApiController {

    @Autowired
    private PublishApiService publishApiService;

    @Autowired
    private DataSetTableService dataSetTableService;

    @GetMapping("/list/{pageSize}/{index}")
    public Map<String, Object> listRecords(@PathVariable Long index, @PathVariable Long pageSize) {
        return publishApiService.listRecords(pageSize, index);
    }


    @GetMapping("/list/dataset/treeSelect")
    public List<DataSetTableDTO> listDataSetTree() {
        return dataSetTableService.listAndGroup(new DataSetTableRequest());
    }

}
