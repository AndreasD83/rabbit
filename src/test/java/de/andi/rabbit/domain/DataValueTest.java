package de.andi.rabbit.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DataValueTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void parseDate() {
       Date date = DataValue.parseDate("23.02.2019 19:53:03");
       assertNotNull(date);
       assertTrue(date.toString().contains("23"));
    }

}