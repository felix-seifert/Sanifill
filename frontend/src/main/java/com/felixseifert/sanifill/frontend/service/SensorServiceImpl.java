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

package com.felixseifert.sanifill.frontend.service;

import com.felixseifert.sanifill.frontend.model.SensorData;
import com.felixseifert.sanifill.frontend.model.SensorDataSma;
import com.felixseifert.sanifill.frontend.model.SensorDataEnriched;
import com.felixseifert.sanifill.frontend.views.sensors.SensorView;
import com.vaadin.flow.component.UI;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SensorServiceImpl implements SensorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensorServiceImpl.class);

    private final Map<UI, SensorView> sensorViews = new HashMap<>();

    @Getter
    private final Map<String, SensorDataEnriched> currentSensorData = new HashMap<>();
    // TODO: Get current values from service `database-storage`

    @Override
    public void sendSensorDataToUis(SensorData sensorData) {
        SensorDataEnriched dataEnriched = putNewSensorData(sensorData);
        sensorViews.keySet().forEach(ui -> ui.access(() -> sensorViews.get(ui).updateSensorData(dataEnriched)));
    }

    private SensorDataEnriched putNewSensorData(SensorData sensorData) {
        synchronized(currentSensorData) {
            SensorDataEnriched dataEnriched = getSensorDataEnriched(sensorData);
            currentSensorData.put(sensorData.getSensorId(), dataEnriched);
            return dataEnriched;
        }
    }

    private SensorDataEnriched getSensorDataEnriched(SensorData sensorData) {
        SensorDataEnriched currentData = currentSensorData.get(sensorData.getSensorId());
        return new SensorDataEnriched(
                sensorData.getSensorId(),
                sensorData.getSensorAddress(),
                sensorData.getSensorPort(),
                sensorData.getDateTime(),
                sensorData.getData(),
                Objects.isNull(currentData) ? null : currentData.getExpectedDepletion());
    }

    @Override
    public void sendSensorDataToUis(SensorDataSma sensorDataSma) {
        SensorDataEnriched dataEnriched = putNewSensorData(sensorDataSma);
        sensorViews.keySet().forEach(ui -> ui.access(() -> sensorViews.get(ui).updateSensorData(dataEnriched)));
    }

    private SensorDataEnriched putNewSensorData(SensorDataSma sensorDataSma) {
        synchronized(currentSensorData) {
            SensorDataEnriched dataEnriched = getSensorDataEnriched(sensorDataSma);
            currentSensorData.put(sensorDataSma.getSensorId(), dataEnriched);
            return dataEnriched;
        }
    }

    private SensorDataEnriched getSensorDataEnriched(SensorDataSma sensorDataSma) {
        SensorDataEnriched currentData = currentSensorData.get(sensorDataSma.getSensorId());
        return new SensorDataEnriched(
                sensorDataSma.getSensorId(),
                Objects.isNull(currentData) ? null : currentData.getSensorAddress(),
                Objects.isNull(currentData) ? null : currentData.getSensorPort(),
                Objects.isNull(currentData) ? null : currentData.getDateTime(),
                Objects.isNull(currentData) ? null : currentData.getData(),
                calculateExpectedDepletion(sensorDataSma, currentSensorData));
    }

    private LocalDateTime calculateExpectedDepletion(SensorDataSma sensorDataSma,
                                                     Map<String, SensorDataEnriched> currentSensorData) {
        // Under assumption of linear decrease of filling, filling(time) = gradient * time + constant
        // We know current time, filling and gradient and calculate constant.
        //      constant = currentFilling - gradient * currentTime
        // For depletion, we assume filling = 0 and calculate time.
        //      projectedTime = -constant / gradient

        double currentFilling = currentSensorData.get(sensorDataSma.getSensorId()).getData();
        double gradient = sensorDataSma.getMovingAverage();
        long currentTime = currentSensorData.get(sensorDataSma.getSensorId()).getDateTime()
                .atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();

        double constant = currentFilling - (gradient * currentTime);
        long projectedTimeSeconds = (long) (0 - (constant / gradient));

        return LocalDateTime.ofInstant(Instant.ofEpochSecond(projectedTimeSeconds), ZoneId.systemDefault());
    }

    @Override
    public void triggerSensorReset(SensorDataEnriched sensorData) {
        WebClient.create()
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host(sensorData.getSensorAddress())
                        .port(sensorData.getSensorPort())
                        .path("/api/v1/sensor").build()) // HTTP only because local
                .retrieve()
                .toEntity(Object.class)
                .subscribe(result -> LOGGER.info("Sensor {} reset at {}",
                        sensorData.getSensorId(),
                        LocalDateTime.now()));
        // TODO: Consider to show notification in frontend about successful refill
    }

    @Override
    public void register(SensorView sensorView) {
        sensorView.getUI().ifPresent(ui -> sensorViews.put(ui, sensorView));
    }

    @Override
    public void unregister(SensorView sensorView) {
        sensorView.getUI().ifPresent(sensorViews::remove);
    }
}
