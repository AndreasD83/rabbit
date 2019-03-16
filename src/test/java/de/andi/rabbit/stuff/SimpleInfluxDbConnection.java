package de.andi.rabbit.stuff;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class SimpleInfluxDbConnection {
    private InfluxDB influx;
    private String server = "container";
    private String port = "8086";
    private String user = "sa";
    private String password = "";
    private String dbName = "test";
    private String measurement = "autogen";
    private String sql = "select * from "+measurement;
    private String retentionPolicy = "autogen";

    @Test
    void process() throws Exception {

        //open connection...
        String url = "http://"+server+":"+port;
        this.influx = InfluxDBFactory.connect(url, user, password);
        this.influx.setLogLevel(InfluxDB.LogLevel.BASIC);
        this.influx.enableBatch(100, 200, TimeUnit.MILLISECONDS); //einzeln schreiben

        //create db
        this.influx.createDatabase(dbName);

        //write
        Point point = Point.measurement(measurement)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("value", 184)
                .build();
        this.influx.write(dbName, retentionPolicy, point);
        this.influx.disableBatch();

        //select
        QueryResult queryResult = this.influx.query(new Query(sql, dbName));
        System.out.println(queryResult.getResults());

        //delete db
        this.influx.deleteDatabase(dbName);

        //close connection
        this.influx.close();
    }
}
