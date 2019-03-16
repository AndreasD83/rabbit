package de.andi.rabbit.gateway;

import de.andi.rabbit.domain.DataValue;

import java.util.List;


public interface DatabaseRepository {
    public void createDatabase();

    public void deleteDatabase();

    public void write(DataValue dataValue);

    public void writeBatch(List<? extends DataValue> dataValues);
}
