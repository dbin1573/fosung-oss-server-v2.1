package com.fosung.cloud.oss.entity;

import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author hi dbin
 * @Date 2020/5/25 6:54
 **/
@Entity
@Table(name = "oss_bucket_v2")
@Getter
@Setter
public class OssBucket extends AppJpaBaseEntity {

    @Column
    private String name;
}
