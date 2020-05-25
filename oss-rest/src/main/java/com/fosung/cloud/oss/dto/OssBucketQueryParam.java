package com.fosung.cloud.oss.dto;

import com.fosung.framework.common.dto.params.AppBasePageParam;
import com.fosung.framework.common.support.dao.entity.AppJpaBaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author hi dbin
 * @Date 2020/5/25 9:09
 **/
@Getter
@Setter
public class OssBucketQueryParam extends AppBasePageParam {

    private Long id;
    private String name;

}
