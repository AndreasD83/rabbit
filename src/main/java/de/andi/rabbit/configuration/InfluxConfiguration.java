package de.andi.rabbit.configuration;

import lombok.Data;
import org.springframework.context.annotation.PropertySource;


@Data
@PropertySource("classpath:application.properties")
public class InfluxConfiguration {

    private String dbName;
    private String queueName;
    private String server;
    private int port;
    private String user;
    private String password;
    private String retentionPolicy;

    public InfluxConfiguration(){
        this.dbName="ultrasonic_data";
        this.server="container";
        this.port=8086;
        this.user="sa";
        this.password="";
        this.retentionPolicy="autogen";
    }

}
