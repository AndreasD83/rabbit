package de.andi.rabbit.writer;

import de.andi.rabbit.gateway.DatabaseRepository;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import de.andi.rabbit.domain.DataValue;

import java.util.List;

public class InfluxDBWriter<DataValue> implements ItemWriter<DataValue> {

    @Autowired
    DatabaseRepository repository;

    @Override
    public void write(List<? extends DataValue> items) throws Exception {

        repository.writeBatch((List<? extends de.andi.rabbit.domain.DataValue>) items);
    }
}
