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

package com.felixseifert.sanifill.frontend.kafka;

import com.felixseifert.sanifill.frontend.model.SensorData;
import com.felixseifert.sanifill.frontend.model.SensorDataSma;
import com.felixseifert.sanifill.frontend.service.SensorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaSensorsListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSensorsListener.class);

    private final SensorService sensorService;

    public KafkaSensorsListener(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @KafkaListener(id = "${sanifill.kafka.sensors.id}",
            topics = "${sanifill.kafka.sensors.topic}",
            containerFactory = "${sanifill.kafka.sensors.container-factory}")
    public void listenToSensorsTopic(SensorData sensorData) {
        LOGGER.info("Received sensorData: {}", sensorData);
        sensorService.sendSensorDataToUis(sensorData);
    }

    @KafkaListener(id = "${sanifill.kafka.sensors-sma.id}",
            topics = "${sanifill.kafka.sensors-sma.topic}",
            containerFactory = "${sanifill.kafka.sensors-sma.container-factory}")
    public void listenToAverageGradients(SensorDataSma sensorDataSma) {
        LOGGER.info("Received gradient average: {}", sensorDataSma);
        sensorService.sendSensorDataToUis(sensorDataSma);
    }
}
