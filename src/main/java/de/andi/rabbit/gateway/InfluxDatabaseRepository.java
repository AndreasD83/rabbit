package de.andi.rabbit.gateway;

import de.andi.rabbit.configuration.InfluxConfiguration;
import de.andi.rabbit.domain.DataValue;
import lombok.Data;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;


import java.util.concurrent.TimeUnit;

@Data
@Repository
public class InfluxDatabaseRepository implements DatabaseRepository {

    private final InfluxDB influx;

    //@Autowired
    //private InfluxConfiguration config;

    //ToDo: add config class
    private String dbName = "firstDb";
    private String server ="container";
    private int port = 8086;
    private String user = "sa";
    private String password = "";
    private String retentionPolicy ="autogen";

    public InfluxDatabaseRepository(){
        System.out.println("InfluxGatewayImpl...\n\n");
        String url = "http://"+server+":"+port;
        this.influx = InfluxDBFactory.connect(url, user, password);
        this.influx.setLogLevel(InfluxDB.LogLevel.BASIC);

    }

    public void createDatabase(){
        this.influx.createDatabase(dbName);
    }

    public void deleteDatabase(){
        this.influx.deleteDatabase(dbName);
    }

    public void write(DataValue dataValue){
        this.influx.enableBatch(100, 200, TimeUnit.MILLISECONDS); //einzeln schreiben
        Point point = Point.measurement("ultrasonic")
                .time(dataValue.getTimestamp().getTime(), TimeUnit.MILLISECONDS)
                .addField("value", dataValue.getValue())
                .build();
        this.influx.write(dbName,retentionPolicy, point);
        this.influx.disableBatch();
    }

    public void writeBatch(List<? extends DataValue> dataValues){

        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .retentionPolicy(retentionPolicy)
                .build();

        dataValues.forEach(dataValue -> {
            batchPoints.point(Point.measurement("ultrasonic")
                    .time(dataValue.getTimestamp().getTime(), TimeUnit.MILLISECONDS)
                    .addField("value", dataValue.getValue())
                    .build());

        });
        this.influx.write(batchPoints);
    }
}
