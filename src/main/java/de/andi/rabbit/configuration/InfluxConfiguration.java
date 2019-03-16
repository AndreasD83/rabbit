package de.andi.rabbit.configuration;

import lombok.Data;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "influxdb")
@Data
public class InfluxConfiguration {


    private String dbName;
    private String server;
    private int port;
    private String user;
    private String password;
    private String retentionPolicy;

}
