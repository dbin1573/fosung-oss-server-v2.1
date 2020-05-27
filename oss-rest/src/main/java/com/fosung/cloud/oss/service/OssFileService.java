package com.fosung.cloud.oss.service;

import com.fosung.cloud.oss.dao.OssFileDao;
import com.fosung.cloud.oss.entity.OssFile;
import com.fosung.framework.dao.support.service.jpa.AppJPABaseDataServiceImpl;
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
            put("name", "name:EQ");
            put("bucketName", "bucketName:EQ");
            put("directory", "directory:EQ");


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
        ossFileOption.deleteFile(ossFile.getBucketName(), ossFile.getDirectory(), ossFile.getName());

    }

    @Transactional
    public void saveDirectory(OssFile ossFile) {
        dirDataInit(ossFile);
        this.save(ossFile);

        ossFileOption.createDir(ossFile.getBucketName(), ossFile.getDirectory(), ossFile.getName());
    }

    private void dirDataInit(OssFile ossFile) {
        ossFile.setSize(0L);
        ossFile.setType("dir");

        String bucket = ossFile.getBucketName();
        String directory = ossFile.getDirectory();
        String path = ossFile.getPath();
        ossFile.setDirectory(StringUtils.isBlank(directory) ? "/" : directory);
        ossFile.setPath(StringUtils.isBlank(path) ? bucket + "/" : path);
    }
}
