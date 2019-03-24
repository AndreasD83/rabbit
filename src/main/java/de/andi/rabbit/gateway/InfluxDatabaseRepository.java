package de.andi.rabbit.gateway;

import de.andi.rabbit.configuration.InfluxConfiguration;
import de.andi.rabbit.domain.DataValue;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.stereotype.Repository;
import java.util.List;


import java.util.concurrent.TimeUnit;

@Data
@Repository
public class InfluxDatabaseRepository implements DatabaseRepository {
    private static final Logger LOGGER = LogManager.getLogger(InfluxDatabaseRepository.class.getName());
    private final InfluxDB influx;

    private static final String MEASUREMENT = "ultrasonic";
    private static final String MEASUREMENTVALUE = "value";
    private static final String WRITETIME = "writetime";

    private InfluxConfiguration config;


    public InfluxDatabaseRepository(){
        this.config = new InfluxConfiguration();
        LOGGER.debug("init InfluxGatewayImpl...");
        String url = "http://"+config.getServer()+":"+config.getPort();
        this.influx = InfluxDBFactory.connect(url, config.getUser(), config.getPassword());
        this.influx.setLogLevel(InfluxDB.LogLevel.BASIC);

    }

    public void createDatabase(){
        this.influx.createDatabase(config.getDbName());
    }

    public void deleteDatabase(){
        this.influx.deleteDatabase(config.getDbName());
    }

    public void write(DataValue dataValue){
        this.influx.enableBatch(10, 200, TimeUnit.MILLISECONDS); //einzeln schreiben
        Point point = Point.measurement(MEASUREMENT)
                .time(dataValue.getTimestamp().getTime(), TimeUnit.MILLISECONDS)
                .addField(MEASUREMENTVALUE, dataValue.getValue())
                .addField(WRITETIME, dataValue.getWriteTime().toString())
                .build();
        LOGGER.debug("write {} to {}", point, config.getDbName());
        this.influx.write(config.getDbName(),config.getRetentionPolicy(), point);
        this.influx.disableBatch();
    }

    public void writeBatch(List<? extends DataValue> dataValues){

        BatchPoints batchPoints = BatchPoints
                .database(config.getDbName())
                .retentionPolicy(config.getRetentionPolicy())
                .build();

        dataValues.forEach(dataValue -> {
            batchPoints.point(Point.measurement(MEASUREMENT)
                    .time(dataValue.getTimestamp().getTime(), TimeUnit.MILLISECONDS)
                    .addField(MEASUREMENTVALUE, dataValue.getValue())
                    .addField(WRITETIME, dataValue.getWriteTime().toString())
                    .build());

        });
        LOGGER.debug("write {} to {}", batchPoints, config.getDbName());
        this.influx.write(batchPoints);
    }
}
