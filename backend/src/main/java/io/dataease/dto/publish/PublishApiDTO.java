package io.dataease.dto.publish;

import lombok.Data;

@Data
public class PublishApiDTO {

    /**
     * ID
     */
    private Integer pubId;

    /**
     * 数据集ID
     */
    private String datasetId;

    /**
     * 数据集名称
     */
    private String datasetName;

    /**
     * 成功返回值包裹体
     */
    private String successWrapper;

    /**
     * 失败返回值包裹
     */
    private String failureWrapper;

    /**
     * 是否允许分页
     */
    private Boolean pageable;

    /**
     * 自定义URL前缀
     */
    private String pubUrlPrefix;

    /**
     * 是否发布
     */
    private Boolean publishEnabled;

}
