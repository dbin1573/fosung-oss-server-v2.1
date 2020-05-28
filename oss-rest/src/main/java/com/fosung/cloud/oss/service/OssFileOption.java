package com.fosung.cloud.oss.service;

import com.fosung.cloud.oss.config.OssConfigProperties;
import com.fosung.cloud.oss.entity.OssFile;
import com.google.common.collect.Maps;
import com.mzlion.core.lang.Assert;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @Author hi dbin
 * @Date 2020/5/22 16:43
 **/

@Service
public class OssFileOption {

    @Autowired
    private OssConfigProperties ossConfigProperties;

    @Autowired
    private OssFileService ossFileService;


    /**
     * 保存返回url
     * 1.配置ip
     * 2.bucket
     * 3.文件名
     * <p>
     * 全局路径
     * <p>
     * 方案1. 列表式存储 上传的文件,不需要关心路径的具体位置 只需要保存url
     * 方案2. 树型存储 ( 一.parentId 二.path类似) 比方案1全面
     */
    @Transactional
    public String saveFile(String bucket, String directory, MultipartFile multipartFile) throws IOException {
        Assert.notNull(bucket, "业务bucket不能为空");
        Assert.notNull(multipartFile, "保存文件为空异常");
        if (null == multipartFile) {
            return "";
        }
        //创建到 nginx配置的具体访问地址
        String absolutePath = getFilPath(bucket, directory, multipartFile.getOriginalFilename(), false);
        //外部访问地址
        String relativePath = getFilPath(bucket, directory, multipartFile.getOriginalFilename(), true);

        createFile(absolutePath, multipartFile);

        String url = getFileUrl(relativePath);

        this.saveOssFile(bucket,directory, relativePath, url, multipartFile);

        return url;
    }

    /**
     * 创建目录
     *
     * @param file
     */
    public boolean createDir(File file) {
        if (!file.exists()) {
           return file.mkdirs();
        }
        return false;
    }


    /**
     * 创建目录
     *
     * @param dirName 目录名
     */
    public boolean createDir(String dirName) {
        String OSS_ROOT_PATH = ossConfigProperties.getRootDirectory();
        String path = isEndBreak(OSS_ROOT_PATH) + dirName;
        File file = new File(path);

        return createDir(file);
    }

    /**
     * 创建目录
     *
     * @param bucketName 空间名称 notNull
     * @param dirName    目录名
     */
    public boolean createDir(String bucketName, String dirName) {
        Assert.isTrue(StringUtils.isNotBlank(bucketName), "业务bucketName不能为空");

        String path =  isEndBreak(bucketName) + isEndBreak(dirName);

        return createDir(path);
    }
    /**
     * 创建目录
     *
     * @param bucketName 空间名称 notNull
     * @param dirName    上级目录名
     * @param name    目录名
     */
    public void createDir(String bucketName, String dirName,String name) {
        Assert.isTrue(StringUtils.isNotBlank(bucketName), "业务bucketName不能为空");

        String path = isEndBreak(bucketName) + isEndBreak(dirName) + name;

        boolean isCreate = createDir(path);
        Assert.isTrue(isCreate,"创建目录失败");
    }

    /**
     * 创建文件
     *
     * @param absolutePath  (全路径)绝对路径
     * @param multipartFile 文件
     * @throws IOException
     */
    private void createFile(String absolutePath, MultipartFile multipartFile) throws IOException {
        File file = new File(absolutePath);
        createDir(file);

        multipartFile.transferTo(file.getAbsoluteFile());

    }

    /**
     * ip+相对路径
     *
     * @param relativePath bucket+目录名+文件名
     * @return
     */
    private String getFileUrl(String relativePath) {
        String OSS_IP = ossConfigProperties.getIp();
        Assert.notNull(OSS_IP, "application*.yml没有配置oss的ip");

        return isEndBreak(OSS_IP) + relativePath;

    }

    private String getFileType(String name) {

        return name.substring(name.lastIndexOf(".") + 1);
    }

    /**
     * @param bucket
     * @param directory
     * @param fileName
     * @param relativePath 获取绝对路径或相对路径
     * @return
     */
    private String getFilPath(String bucket, String directory, String fileName, Boolean relativePath) {
        String OSS_ROOT_PATH = ossConfigProperties.getRootDirectory();
        StringBuilder pathName = new StringBuilder();
        if (!relativePath) {
            Assert.notNull(OSS_ROOT_PATH, "application*.yml没有配置oss的根路径OSS_ROOT_PATH");
            pathName.append(isEndBreak(OSS_ROOT_PATH));
        }
        pathName.append(isEndBreak(bucket));
        pathName.append(isEndBreak(directory));
        pathName.append(fileName);

        return pathName.toString();
    }


    /**
     * 绝对路径:(保存\删除)
     * 相对路径 包名:(查询)
     *
     * @param directory     包名
     * @param relativePath  文件相对路径
     * @param url           访问的url(其中是相对路径)
     * @param multipartFile 文件
     */
    private void saveOssFile(String bucket,String directory, String relativePath, String url, MultipartFile multipartFile) {
        OssFile ossFile = new OssFile();

        ossFile.setBucketName(bucket);
        ossFile.setDirectory(StringUtils.isBlank(directory) ? "/" : directory);
        ossFile.setPath(relativePath);
        ossFile.setUrl(url);
        ossFile.setType("file");

        Assert.notNull(multipartFile, "保存记录异常,文件为空");
        ossFile.setName(multipartFile.getOriginalFilename());
        Long size = multipartFile.getSize() / 1024;
        ossFile.setSize(size);

        boolean notExist = checkNotExist(bucket, directory, multipartFile.getOriginalFilename());
        if (notExist) {
            ossFileService.save(ossFile);
        }
    }

    private boolean checkNotExist(String bucket, String directory, String originalFilename) {
        Map<String, Object> searchParam = Maps.newHashMap();
        searchParam.put("bucketName", bucket);
        searchParam.put("directory", directory);
        searchParam.put("name", originalFilename);
        boolean notExist = !ossFileService.isExist(searchParam);
//        Assert.isTrue(notExist, "上传失败: 当前目录下 该文件已存在");
        return notExist;
    }

    /**
     * 路径以"/"结尾
     * 路径为空返回空字符
     *
     * @param path
     * @return
     */
    public String isEndBreak(String path) {
        if (StringUtils.isBlank(path) || path.equals("/")) {
            return "";
        }
        if (!StringUtils.endsWithAny(path, "/", "\\")) {
            return path + "/";
        }
        return path;
    }

    /**
     * 清空空格 null
     * @param path
     * @return
     */
    public String isSpace(String path) {

        if (StringUtils.isBlank(path)) {
            return "";
        }
        if (path.endsWith("/")||path.endsWith("\\")) {
            return path.trim().substring(0,path.lastIndexOf("/")-2);
        }
        return path;
    }

    /**
     * 删除
     *
     * @param directory 目录名
     *                  自动拼接绝对路径
     */
    public boolean deleteFile(String bucket, String directory, String fileName) {
        Assert.notNull(bucket,"bucket为空");
        Assert.notNull(directory,"directory为空");
        Assert.notNull(fileName,"fileName为空");
        String absolutePath = getFilPath(bucket, directory, fileName, false);

        File file = new File(absolutePath);

        if (file.exists()) {
            return file.delete();
        }

        return false;
    }

    public boolean deleteDir(String pathName) {
        String OSS_ROOT_PATH = ossConfigProperties.getRootDirectory();
        Assert.notNull(OSS_ROOT_PATH, "application*.yml没有配置oss的根路径OSS_ROOT_PATH");
        Assert.isTrue(StringUtils.isNotBlank(pathName), "路径名为空");

        String path = isEndBreak(OSS_ROOT_PATH) + isSpace(pathName);
        File file = new File(path);
        boolean notExist = !file.exists();
        if (notExist) {
            return true;
        }
        return file.delete();
    }

    public boolean rename(String pathName, String newName) {
        String OSS_ROOT_PATH = ossConfigProperties.getRootDirectory();
        Assert.notNull(OSS_ROOT_PATH, "application*.yml没有配置oss的根路径OSS_ROOT_PATH");

        String path = isEndBreak(OSS_ROOT_PATH) + isSpace(pathName);
        String newPath = isEndBreak(OSS_ROOT_PATH) + isSpace(newName);
        File file = new File(path);

        return file.renameTo(new File(newPath));
    }


}
