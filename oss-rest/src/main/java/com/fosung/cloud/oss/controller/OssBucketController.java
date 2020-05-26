package com.fosung.cloud.oss.controller;

import com.fosung.cloud.oss.dto.OssBucketQueryParam;
import com.fosung.cloud.oss.entity.OssBucket;
import com.fosung.cloud.oss.service.OssBucketService;
import com.fosung.framework.common.dto.UtilDTO;
import com.fosung.framework.web.http.AppIBaseController;
import com.fosung.framework.web.http.ResponseParam;
import com.mzlion.core.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author hi dbin
 * @Date 2020/5/25 7:05
 **/

@RestController
@RequestMapping("/api/oss/bucket")
public class OssBucketController extends AppIBaseController {


    @Autowired
    private OssBucketService ossBucketService;

    @RequestMapping("/save")
    public ResponseParam save(@RequestBody OssBucket ossBucket) {
        Assert.isTrue(!StringUtils.isBlank(ossBucket.getName()), "bucket的业务name不能为空");

        //todo 校验:出现多个  ///  //  /

        if (ossBucket.getId() == null) {
            ossBucketService.saveBucket(ossBucket);
        } else {

            ossBucketService.updateBucket(ossBucket);
        }
        return ResponseParam.success();
    }

    @RequestMapping("/get")
    public ResponseParam get(@RequestBody OssBucketQueryParam ossBucketQueryParam) {
        Assert.notNull(ossBucketQueryParam.getId(), "bucket的业务id不能为空");

        OssBucket ossBucket = ossBucketService.get(ossBucketQueryParam.getId());

        return ResponseParam.success().data(ossBucket);
    }

    @RequestMapping("/list")
    public ResponseParam list() {

        List<OssBucket> ossBuckets = ossBucketService.queryAll(null, new String[]{"id_asc"});

        return ResponseParam.success().datalist(ossBuckets);
    }

    @RequestMapping("/query")
    public ResponseParam query(@RequestBody OssBucketQueryParam ossBucketQueryParam) {
        Map<String, Object> searchMap = UtilDTO.toDTOExcludeFields(ossBucketQueryParam,null);
        Page<OssBucket> page = ossBucketService.queryByPage(searchMap, ossBucketQueryParam.getPageNum(), ossBucketQueryParam.getPageSize(), new String[]{"id_asc"});

        return ResponseParam.success()
                .pageParam(page)
                .datalist(page.getContent());
    }


    @RequestMapping("/delete")
    public ResponseParam delete(@RequestBody OssBucket ossBucket) {
        Assert.notNull(ossBucket.getId(), "bucket的业务id不能为空");
        Assert.isTrue(StringUtils.isBlank(ossBucket.getName()), "bucket的业务name不能为空");

        OssBucket bucketInfo = ossBucketService.get(ossBucket.getId());
        Assert.notNull(bucketInfo, "bucketInfo为空");

        ossBucketService.deleteBucket(bucketInfo);

        return ResponseParam.success();
    }


}
