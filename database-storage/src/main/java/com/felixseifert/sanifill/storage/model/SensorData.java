/*
 * Copyright (C) 2021 Felix Seifert <mail@felix-seifert.com> (https://felix-seifert.com)
 *
 * This programme is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any
 * later version.
 *
 * This programme is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.felixseifert.sanifill.storage.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.panache.common.Sort;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sensor_data")
@RegisterForReflection
@ToString
public class SensorData extends PanacheEntity {

    @NotBlank(message = "Sensor ID '${validatedValue}' of sensor data must not be blank")
    @Column(nullable = false)
    public String sensorId;

    @NotBlank(message = "Sensor address '${validatedValue}' of sensor data must not be blank")
    @Column(nullable = false)
    public String sensorAddress;

    @NotNull(message = "Sensor port '${validatedValue}' of sensor data must not be null")
    @Column(nullable = false)
    public Integer sensorPort;

    @NotNull(message = "Date time '${validatedValue}' of when sensor data was produced must not be null")
    @Column(nullable = false)
    public LocalDateTime dateTime;

    @NotNull(message = "Data '${validatedValue}' of sensor data must not be null")
    @Column(nullable = false)
    public Double data;

    public static SensorData add(String sensorId, String sensorAddress, Integer sensorPort,
                           LocalDateTime dateTime, Double data) {
        SensorData sensorData = new SensorData();
        sensorData.sensorId = sensorId;
        sensorData.sensorAddress = sensorAddress;
        sensorData.sensorPort = sensorPort;
        sensorData.dateTime = dateTime;
        sensorData.data = data;
        sensorData.persist();
        return sensorData;
    }

    public static SensorData addIncoming(SensorDataIncoming sensorDataIncoming) {
        return add(sensorDataIncoming.getSensorId(),
                sensorDataIncoming.getSensorAddress(),
                sensorDataIncoming.getSensorPort(),
                sensorDataIncoming.getDateTime(),
                sensorDataIncoming.getData());
    }

    public static List<SensorData> findAllBySensorId(String sensorId) {
        return list("sensorId", Sort.by("dateTime"), sensorId);
    }
}
