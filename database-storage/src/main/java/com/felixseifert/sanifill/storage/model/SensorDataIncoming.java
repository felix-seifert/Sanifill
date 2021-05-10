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

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class SensorDataIncoming {

    @NotBlank(message = "Sensor ID '${validatedValue}' of sensor data must not be blank")
    private String sensorId;

    @NotBlank(message = "Sensor address '${validatedValue}' of sensor data must not be blank")
    private String sensorAddress;

    @NotNull(message = "Sensor port '${validatedValue}' of sensor data must not be null")
    private Integer sensorPort;

    @NotNull(message = "Date time '${validatedValue}' of when sensor data was produced must not be null")
    private LocalDateTime dateTime;

    @NotNull(message = "Data '${validatedValue}' of sensor data must not be null")
    private Double data;
}
