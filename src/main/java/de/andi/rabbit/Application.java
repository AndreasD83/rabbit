package de.andi.rabbit;

import de.andi.rabbit.configuration.InfluxConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableConfigurationProperties(InfluxConfiguration.class)
public class Application {
    public static void main( String[] args ) throws Exception
    {
        SpringApplication.run( Application.class, args );
    }
}
