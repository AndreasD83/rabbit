package de.andi.rabbit.configuration;

import lombok.Data;




@Data
public class InfluxConfiguration {

    private String dbName;
    private String queueName;
    private String server;
    private int port;
    private String user;
    private String password;
    private String retentionPolicy;

    public InfluxConfiguration(){
        this.dbName="ultrasonic_database";
        this.server="container";
        this.port=8086;
        this.user="sa";
        this.password="";
        this.retentionPolicy="autogen";
    }

}
