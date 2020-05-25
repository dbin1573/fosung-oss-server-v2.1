package com.fosung.cloud.oss.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author hi dbin
 * @Date 2020/5/22 17:02
 **/

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.oss")
public class OssConfigProperties {

    private String ip;
    private String rootDirectory;
}
