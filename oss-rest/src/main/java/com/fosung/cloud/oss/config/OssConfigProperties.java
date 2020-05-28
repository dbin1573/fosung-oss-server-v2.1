package com.fosung.cloud.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author hi dbin
 * @Date 2020/5/22 17:02
 **/

@Data
@ConfigurationProperties(prefix = "app.oss")
public class OssConfigProperties {

    private String ip;
    private String rootDirectory;
}
