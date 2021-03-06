package com.fosung.cloud.oss.api;

import com.fosung.cloud.oss.service.OssFileOption;
import com.fosung.framework.web.http.ResponseParam;
import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Map;

/**
 * @Author hi dbin
 * @Date 2020/5/22 9:43
 **/

@RestController
@RequestMapping("/api/oss/single/{bucket}")
public class OssFileApi extends OssBaseController {


    @Autowired
    private OssFileOption ossFileOption;

    public static void main(String[] args) {
        final String OSS_PATH = "D:/Temp/uploadFile/";
        File file = new File(OSS_PATH + "/cloud-sys/" + "5f.png");
        if (file.exists()) {
            file.delete();
        }
    }

    //io.minio.*.Upload
    //列出已存储的照片
    //删除

    @SneakyThrows
    @RequestMapping("/upload")
    public ResponseParam upload(@PathVariable String bucket,
                                @RequestParam(value = "directory", required = false, defaultValue = "/") String directory,
                                @RequestParam(value = "file") MultipartFile multipartFile) {

        String url = ossFileOption.saveFile(bucket, directory, multipartFile);

        Map<String, Object> map = ImmutableMap.of("url", url);
        return ResponseParam.success().data(map);
    }




}
