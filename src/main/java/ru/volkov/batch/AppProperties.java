package ru.volkov.batch;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "uko.ppk")
public class AppProperties {

    private String tempDir;
    private String remoteBaseDir;
    private String sotialUkoFilePrefix;
    private String sotialUkoFileSuffix;
}
