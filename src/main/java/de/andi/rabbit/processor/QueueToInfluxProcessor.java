package de.andi.rabbit.processor;

import de.andi.rabbit.domain.DataValue;
import org.springframework.batch.item.ItemProcessor;

import java.util.Arrays;

public class QueueToInfluxProcessor implements ItemProcessor<Object, DataValue> {
    @Override
    public DataValue process(Object item) throws Exception {
        DataValue result = new DataValue(new String((byte[]) item));
        System.out.println(result);
        return result;
    }
}
