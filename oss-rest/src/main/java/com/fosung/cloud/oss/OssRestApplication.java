package com.fosung.cloud.oss;

import com.fosung.cloud.oss.config.OssConfigProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;


//@EnableConfigurationProperties(OssConfigProperties.class)
@SpringBootApplication
public class OssRestApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                new SpringApplicationBuilder( OssRestApplication.class ).run() ;

    }

}
