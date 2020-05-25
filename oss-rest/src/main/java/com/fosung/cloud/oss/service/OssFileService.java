package com.fosung.cloud.oss.service;

import com.fosung.cloud.oss.dao.OssFileDao;
import com.fosung.cloud.oss.dto.OssFileQueryParam;
import com.fosung.cloud.oss.entity.OssFile;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
import com.mzlion.core.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author hi dbin
 * @Date 2020/5/22 15:32
 **/

@Service
public class OssFileService extends AppJPABaseDataServiceImpl<OssFile, OssFileDao> {

    Map<String, String> queryExpressions = new HashMap<String, String>() {
        {
            put("id", "id:EQ");
            put("name", "name:LLIKE");
            put("bucketName", "bucketName:EQ");
            put("directory", "directory:EQ");
            put("path", "path:LLIKE");

        }
    };
    @Autowired
    private OssFileOption ossFileOption;

    @Override
    public Map<String, String> getQueryExpressions() {
        return queryExpressions;
    }

    @Transactional
    public void deleteOssFile(OssFile ossFile) {

        this.entityDao.delete(ossFile);
        //调用文件删除
        ossFileOption.deleteFile(ossFile.getBucketName(),ossFile.getPath(),ossFile.getName());

    }

    @Transactional
    public void saveDirectory(OssFile ossFile) {
        this.save(ossFile);

        ossFileOption.createDir(ossFile.getBucketName(),ossFile.getDirectory(),ossFile.getName());
    }
}
