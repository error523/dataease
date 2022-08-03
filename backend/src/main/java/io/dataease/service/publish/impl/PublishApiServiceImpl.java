package io.dataease.service.publish.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.dataease.commons.exception.DEException;
import io.dataease.commons.utils.CommonBeanFactory;
import io.dataease.commons.utils.TableUtils;
import io.dataease.dto.publish.PublishApiDTO;
import io.dataease.i18n.Translator;
import io.dataease.plugins.common.base.domain.DatasetTable;
import io.dataease.plugins.common.base.domain.DatasetTableField;
import io.dataease.plugins.common.base.domain.Datasource;
import io.dataease.plugins.common.base.domain.PublishApi;
import io.dataease.plugins.common.base.mapper.DatasetTableMapper;
import io.dataease.plugins.common.base.mapper.DatasourceMapper;
import io.dataease.plugins.common.base.mapper.PublishApiMapper;
import io.dataease.plugins.common.request.datasource.DatasourceRequest;
import io.dataease.plugins.datasource.provider.Provider;
import io.dataease.plugins.datasource.query.QueryProvider;
import io.dataease.provider.ProviderFactory;
import io.dataease.provider.datasource.JdbcProvider;
import io.dataease.service.dataset.DataSetTableFieldsService;
import io.dataease.service.engine.EngineService;
import io.dataease.service.publish.PublishApiService;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author dever
 * @description 针对表【z_t_publish_api(自定义发布数据API表)】的数据库操作Service实现
 * @createDate 2022-06-29 13:06:07
 */
@Service
@Log4j2
public class PublishApiServiceImpl extends ServiceImpl<PublishApiMapper, PublishApi>
        implements PublishApiService {

    @Autowired
    private DatasetTableMapper datasetTableMapper;

    @Autowired
    private DataSetTableFieldsService dataSetTableFieldsService;

    @Autowired
    private EngineService engineService;

    @Autowired
    private DatasourceMapper datasourceMapper;

    @Override
    public Map<String, Object> listRecords(Long pageSize, Long index) {
        IPage<PublishApi> publishApiPage = page(new Page<PublishApi>()
                .setPages(pageSize)
                .setCurrent(index));
        List<PublishApi> list = publishApiPage.getRecords();
        List<PublishApiDTO> rsList = list.stream().map(l -> {
            PublishApiDTO publishApiDTO = new PublishApiDTO();
            BeanUtil.copyProperties(l, publishApiDTO, false);
            publishApiDTO.setDatasetName(datasetTableMapper.selectByPrimaryKey(l.getDatasetId()).getName());
            return publishApiDTO;
        }).collect(Collectors.toList());
        return Map.of("records", rsList,
                "total", publishApiPage.getTotal());
    }

    @Override
    public List<Map<String, Object>> queryDataSetData(String prefixUri, Integer publishId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        List<String[]> data = new ArrayList<>();
        PublishApi publishApi = getById(publishId);
        if (null != publishApi && publishApi.getPublishEnabled()) {
            if (prefixUri.equals(publishApi.getPubUrlPrefix())) {
                DatasetTable datasetTable = datasetTableMapper.selectByPrimaryKey(publishApi.getDatasetId());
                if(datasetTable.getMode() == 0) {
                    log.info("{} 直连模式！", datasetTable);
                    Datasource ds = datasourceMapper.selectByPrimaryKey(datasetTable.getDataSourceId());
                    if (ObjectUtils.isEmpty(ds)) {
                        throw new RuntimeException(Translator.get("i18n_datasource_delete"));
                    }
                    if (StringUtils.isNotEmpty(ds.getStatus()) && ds.getStatus().equalsIgnoreCase("Error")) {
                        throw new Exception(Translator.get("i18n_invalid_ds"));
                    }
                    DatasetTableField datasetTableField = DatasetTableField.builder().tableId(datasetTable.getId())
                            .checked(Boolean.TRUE).build();
                    List<DatasetTableField> fields = dataSetTableFieldsService.list(datasetTableField);
                    log.info("== 列名为 {}", fields);
                    Provider datasourceProvider = ProviderFactory.getProvider(ds.getType());
                    DatasourceRequest datasourceRequest = new DatasourceRequest();
                    datasourceRequest.setDatasource(ds);
                    String sql = (String) JSONUtil.toBean(datasetTable.getInfo(), Map.class).get("sql");
                    log.info("== 待执行SQL为 ：{}", sql);
                    datasourceRequest.setQuery(sql);
                    try {
                        if (null != datasourceProvider) {
                            data.addAll(datasourceProvider.getData(datasourceRequest));
                            List<Map<String, Object>> rs;
                            rs = data.stream().map(d -> {
                                Map<String, Object> m = new HashMap<>();
                                fields.forEach(f -> m.put(f.getName(), d[f.getColumnIndex()]));
                                return m;
                            }).collect(Collectors.toList());
                            log.info("===direct execute data {} ===", rs);
                            return rs;
                        } else {
                            throw new RuntimeException("--- 无法找到对于的数据库链接 ---");
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        DEException.throwException(Translator.get("i18n_ds_error") + "->" + e.getMessage());
                    }
                }else {
                    String datasetId = publishApi.getDatasetId();
                    String dorisTable = TableUtils.tableName(datasetId);
                    DatasetTableField datasetTableField = DatasetTableField.builder().tableId(datasetId)
                            .checked(Boolean.TRUE).build();
                    List<DatasetTableField> dorisFields = dataSetTableFieldsService.list(datasetTableField);
                    Datasource ds = engineService.getDeEngine();
                    JdbcProvider jdbcProvider = CommonBeanFactory.getBean(JdbcProvider.class);
                    DatasourceRequest datasourceRequest = new DatasourceRequest();
                    datasourceRequest.setDatasource(ds);
                    QueryProvider qp = ProviderFactory.getQueryProvider(ds.getType());
                    datasourceRequest.setQuery(
                            qp.createQuerySQL(dorisTable, dorisFields, false, ds, null));
                    map.put("sql", datasourceRequest.getQuery());
                    try {
                        if (null != jdbcProvider) {
                            data.addAll(jdbcProvider.getData(datasourceRequest));
                            List<Map<String, Object>> rs;
                            rs = data.stream().map(d -> {
                                Map<String, Object> m = new HashMap<>();
                                dorisFields.forEach(fields -> m.put(fields.getName(), d[fields.getColumnIndex()]));
                                return m;
                            }).collect(Collectors.toList());
                            log.info("===doris execute map {}, data {} ===", map, data);
                            return rs;
                        } else {
                            throw new RuntimeException("--- 无法找到对于的数据库链接 ---");
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        DEException.throwException(Translator.get("i18n_ds_error") + "->" + e.getMessage());
                    }
                }
            }
        }
        return null;
    }
}
