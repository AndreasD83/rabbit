package de.andi.rabbit.domain;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class DataValue {

    private Date timestamp;
    private Float value;

    public DataValue(String item){
        this.timestamp = DataValue.parseDate(item.split("timestamp:")[1].split(",")[0].trim());
        this.value = Float.valueOf(item.split(",")[1].split(":")[1].trim());
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy hh:mm:ss").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "DataValue{" +
                "timestamp=" + timestamp +
                ", value=" + value +
                '}';
    }
}
