package io.dataease.controller.publish;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.dataease.controller.request.dataset.DataSetTableRequest;
import io.dataease.dto.dataset.DataSetTableDTO;
import io.dataease.plugins.common.base.domain.PublishApi;
import io.dataease.service.dataset.DataSetTableService;
import io.dataease.service.publish.PublishApiService;
import io.swagger.annotations.Api;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "数据发布：发布API接口")
@ApiSupport(order = 99)
@RestController
@RequestMapping("/publish/api")
@Log4j2
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

    @GetMapping("/export/dataset/**/{publishId}")
    public List<Map<String, Object>> queryDataSet(@PathVariable Integer publishId, HttpServletRequest request) {
        final String path =
                request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString();
        final String bestMatchingPattern =
                request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
        String arguments = new AntPathMatcher().extractPathWithinPattern(bestMatchingPattern, path);
        log.info("== match uri is {}", arguments);
        log.info("== publish ID is {}", publishId);
        String prefixUri = StrUtil.removeSuffix(arguments, "/" + publishId);
        log.info("== prefixUrl is {}", prefixUri);
        try {
            return publishApiService.queryDataSetData("/" + prefixUri, publishId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
