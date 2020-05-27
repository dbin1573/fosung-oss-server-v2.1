package com.fosung.cloud.oss.controller;

import com.fosung.cloud.oss.dto.OssFileQueryParam;
import com.fosung.cloud.oss.entity.OssFile;
import com.fosung.cloud.oss.service.OssFileService;
import com.fosung.framework.common.dto.UtilDTO;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.google.common.collect.Sets;
import com.mzlion.core.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author hi dbin
 * @Date 2020/5/22 9:46
 **/

@RestController
@RequestMapping("/api/oss/file")
public class OssFileController extends AppIBaseController {

    @Autowired
    private OssFileService ossFileService;

    /**
     * 文件列表
     *
     * @param ossFileQueryParam
     * @return
     */
    @RequestMapping(value = "/query")
    public ResponseParam query(@RequestBody OssFileQueryParam ossFileQueryParam) {
        Assert.notNull(ossFileQueryParam.getBucketName(), "业务bucketName不能为空");
        if (StringUtils.isBlank(ossFileQueryParam.getDirectory())) {
            ossFileQueryParam.setDirectory("/");
        }

        Map<String, Object> queryParam = UtilDTO.toDTO(ossFileQueryParam, Sets.newHashSet("bucketName","directory"));

        Page<OssFile> ossFiles = ossFileService.queryByPage(queryParam, ossFileQueryParam.getPageNum(), ossFileQueryParam.getPageSize());

        return ResponseParam.success()
                .pageParam(ossFiles)
                .datalist(ossFiles.getContent());

    }

/*    *//**
     * 列表树
     *
     * @param ossFileQueryParam
     * @return
     *//*
    @RequestMapping(value = "/lazy/list")
    public ResponseParam tree(@RequestBody OssFileQueryParam ossFileQueryParam) {
        Assert.notNull(ossFileQueryParam.getBucketName(), "业务bucketName不能为空");
        if (StringUtils.isBlank(ossFileQueryParam.getDirectory())) {
            ossFileQueryParam.setDirectory("/");
        }
        Map<String, Object> searchMap = UtilDTO.toDTOExcludeFields(ossFileQueryParam, null);

        Page<OssFile> ossFiles = ossFileService.queryByPage(searchMap, ossFileQueryParam.getPageNum(), ossFileQueryParam.getPageSize());


        return ResponseParam.success()
                .pageParam(ossFiles)
                .datalist(ossFiles);
    }*/

    /**
     * 请求返回
     * session 是个对象 会返回sessionId
     * session 过期重定向到登录/login
     * 浏览器cookie不用set 服务端可以用response中获取cookie
     * <p>
     * redis保存token
     * <p>
     * 多个应用:
     * 其他应用  seeion  共享->登录
     * <p>
     * oauth2过程
     */

    @RequestMapping(value = "/get")
    public ResponseParam get(@RequestBody OssFileQueryParam ossFileQueryParam) {
        Assert.notNull(ossFileQueryParam.getId(), "业务id不能为空");

        OssFile ossFile = ossFileService.get(ossFileQueryParam.getId());

        return ResponseParam.success()
                .data(ossFile);
    }

    @RequestMapping(value = "/dir/create")
    public ResponseParam save(@RequestBody OssFile ossFile) {
        Assert.notNull(ossFile.getBucketName(), "业务bucketName不能为空");
        Assert.notNull(ossFile.getName(),"名称name不能为空");

        ossFileService.saveDirectory(ossFile);

        return ResponseParam.success()
                .data(ossFile);
    }

    /**
     * @param ossFile
     * @return
     */
    @RequestMapping(value = "/delete")
    public ResponseParam delete(@RequestBody OssFile ossFile) {
        Long id = ossFile.getId();
        Assert.notNull(id, "业务id不能为空");

        ossFile = ossFileService.get(id);
        ossFileService.deleteOssFile(ossFile);

        return ResponseParam.success();
    }


}
