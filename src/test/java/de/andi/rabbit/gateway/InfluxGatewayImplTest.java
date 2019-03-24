package de.andi.rabbit.gateway;

import de.andi.rabbit.Application;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = { Application.class })
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource( locations = "classpath:integration-test/application.properties")
public class InfluxGatewayImplTest {

    @Autowired
    private DatabaseRepository gateway;

   // @Test
    public void write(){
        gateway.createDatabase();
        System.out.println("write...\n\n");
        gateway.write(null);
        assertTrue(true);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("tearDown...\n\n");
        gateway.deleteDatabase();
    }
}