package com.fosung.cloud.oss.entity;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author hi dbin
 * @Date 2020/5/22 15:34
 **/


@Entity
@Table(name = "oss_file_v2")
@Getter
@Setter
public class OssFile extends AppJpaBaseEntity {

    /**
     * 路径头
     */
    @Column(name="bucket_name")
    String bucketName;

    /**
     * 文件夹
     * 查询时使用
     */
    @Column(name = "directory")
    String directory;

    @Column(name = "name")
    String name;

    @Column(name = "size")
    Integer size = 0;

    /*文件大小单位*/

    /**
     * 区分目录还是文件 (file/dir)
     */
    @Column(name = "type")
    String type = "file";

    @Column(name = "path")
    String path;

    @Column(name = "url")
    String url;


}
