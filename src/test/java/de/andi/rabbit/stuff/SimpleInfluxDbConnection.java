package de.andi.rabbit.stuff;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SimpleInfluxDbConnection {
    private InfluxDB influx;
    private String server = "container";
    private String port = "8086";
    private String user = "sa";
    private String password = "";
    private String dbName = "test";
    private String measurement = "ultrasonic";
    private String sql = "select * from "+measurement;
    private String retentionPolicy = "autogen";
    private int batchCount = 10000;


    //@Test
    void processOneValue() throws Exception {

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
                .addField("value", getRandomNumberInRange(10,200))
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

    @Test
    void processBatchValue() throws Exception {

        //open connection...
        String url = "http://"+server+":"+port;
        this.influx = InfluxDBFactory.connect(url, user, password);
        this.influx.setLogLevel(InfluxDB.LogLevel.BASIC);
        //this.influx.enableBatch(100, 200, TimeUnit.MILLISECONDS); //einzeln schreiben

        //create db
        this.influx.createDatabase(dbName);

        //init BatchPoints
        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .retentionPolicy(retentionPolicy)
                .build();

        //build points
        for(int i=0;i< batchCount;i++){
            Point point = Point.measurement(measurement)
                    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                    .addField("value", getRandomNumberInRange(10,200))
                    .build();
            batchPoints.point(point);
        }


        //write
        this.influx.write(batchPoints);
        this.influx.disableBatch();

        //select
        QueryResult queryResult = this.influx.query(new Query(sql, dbName));
        System.out.println(queryResult.getResults());

        //delete db
        this.influx.deleteDatabase(dbName);

        //close connection
        this.influx.close();
    }

    private long getTodayMinusDays(int days)
    {
        return Instant.now().getEpochSecond();
    }
    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
