package de.andi.rabbit.stuff;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class FillSomeQueue {
    private final static String QUEUE_NAME = "data_temp";

    @BeforeEach
    void setUp() {
    }

    //@RepeatedTest(value = 10000)
    void sendToAmqp() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("openhabianpi");
        factory.setUsername("ultra");
        factory.setPassword("ultra");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "timestamp: "+getDate()+", value: " +getRandomNumberInRange(180,184);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }

    private static int i = 0;
    private String getDate(){
        Calendar calendar = Calendar.getInstance(); // this would default to now
        calendar.add(Calendar.DAY_OF_MONTH, -getRandomNumberInRange(1,10000));
        String result =  new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").format(calendar.getTime()).toString();
        System.out.println(result);
        return result;
    }

    private static int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}