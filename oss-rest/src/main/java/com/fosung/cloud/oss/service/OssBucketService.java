package com.fosung.cloud.oss.service;

import com.fosung.cloud.oss.config.OssConfigProperties;
import com.fosung.cloud.oss.dao.OssBucketDao;
import com.fosung.cloud.oss.entity.OssBucket;
import com.fosung.framework.common.dto.UtilDTO;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.google.common.collect.Sets;
import com.mzlion.core.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author hi dbin
 * @Date 2020/5/25 7:10
 **/
@Service
public class OssBucketService extends AppJPABaseDataServiceImpl<OssBucket, OssBucketDao> {

    Map<String, String> queryExpression = new HashMap<String, String>() {
        {
            put("name", "name:EQ");
            put("bucketName", "name:EQ");
        }
    };
    @Autowired
    private OssFileOption ossFileOption;


    @Override
    public Map<String, String> getQueryExpressions() {
        return queryExpression;
    }

    @Transactional
    public void saveBucket(OssBucket ossBucket) {

        this.save(ossBucket);

        ossFileOption.createDir(ossBucket.getName());

    }

    @Transactional
    public void deleteBucket(OssBucket ossBucket) {
        this.entityDao.delete(ossBucket);
//        OssBucket ossBucketInfo = this.get(ossBucket.getId());
//        Assert.notNull(ossBucketInfo,"目录不存在");

        boolean del = ossFileOption.deleteDir(ossBucket.getName());
        Assert.isTrue(del,"删除失败 此目录下可能存在目录或文件");
    }

    @Transactional
    public void updateBucket(OssBucket ossBucket) {
        OssBucket oldBucket =  this.get(ossBucket.getId());
        this.entityDao.update(ossBucket, Sets.newHashSet("name"));

        ossFileOption.rename(oldBucket.getName(),ossBucket.getName());
    }




}
