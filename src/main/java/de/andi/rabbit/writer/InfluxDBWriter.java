package de.andi.rabbit.writer;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class InfluxDBWriter<DataValue> implements ItemWriter<DataValue> {
    @Override
    public void write(List<? extends DataValue> items) throws Exception {

    }
}
