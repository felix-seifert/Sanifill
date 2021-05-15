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

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_data")
@RegisterForReflection
@Data
public class SensorData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sensorDataSeq")
    private Long id;

    @NotBlank(message = "Sensor ID '${validatedValue}' of sensor data must not be blank")
    @Column(nullable = false)
    private String sensorId;

    @NotBlank(message = "Sensor address '${validatedValue}' of sensor data must not be blank")
    @Column(nullable = false)
    private String sensorAddress;

    @NotNull(message = "Sensor port '${validatedValue}' of sensor data must not be null")
    @Column(nullable = false)
    private Integer sensorPort;

    @NotNull(message = "Date time '${validatedValue}' of when sensor data was produced must not be null")
    @Column(nullable = false)
    private LocalDateTime dateTime;

    @NotNull(message = "Data '${validatedValue}' of sensor data must not be null")
    @Column(nullable = false)
    private Double data;

    public static SensorData constructFromSensorDataIncoming(SensorDataIncoming sensorDataIncoming) {
        SensorData sensorData = new SensorData();
        sensorData.setSensorId(sensorDataIncoming.getSensorId());
        sensorData.setSensorAddress(sensorDataIncoming.getSensorAddress());
        sensorData.setSensorPort(sensorDataIncoming.getSensorPort());
        sensorData.setDateTime(sensorDataIncoming.getDateTime());
        sensorData.setData(sensorDataIncoming.getData());
        return sensorData;
    }
}
