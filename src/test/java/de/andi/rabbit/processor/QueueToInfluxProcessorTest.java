package de.andi.rabbit.processor;

import de.andi.rabbit.domain.DataValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueueToInfluxProcessorTest {
    private QueueToInfluxProcessor processor;
    private String value;
    private String example;

    @BeforeEach
    void setUp() {
        value = "180.4567";
        example = "timestamp: 23.02.2019 19:53:03, value: "+value;
    }

    @Test
    void process() throws Exception {
        processor = new QueueToInfluxProcessor();


        byte[] bytes = example.getBytes();

        DataValue dataValue = processor.process(bytes);
        assertNotNull(dataValue);
        assertNotNull(dataValue.getTimestamp());
        assertNotNull(dataValue.getValue());
        assertEquals(value, dataValue.getValue().toString());

    }
}