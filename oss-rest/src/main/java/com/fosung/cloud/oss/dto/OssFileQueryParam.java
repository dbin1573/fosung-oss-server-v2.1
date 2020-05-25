package com.fosung.cloud.oss.dto;

import com.fosung.framework.common.dto.params.AppBasePageParam;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OssFileQueryParam extends AppBasePageParam {

    private Long id;

    /**
     * 包名
     */
    private String bucketName;

    /**
     * 目录名
     */
    private String directory;

    /**
     * 文件名
     */
    private String fileName;

}
