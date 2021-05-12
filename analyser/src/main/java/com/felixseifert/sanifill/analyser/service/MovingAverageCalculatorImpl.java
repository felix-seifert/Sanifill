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

package com.felixseifert.sanifill.analyser.service;

import com.felixseifert.sanifill.analyser.util.MovingAverageStatistics;
import com.felixseifert.sanifill.analyser.model.SensorData;
import com.felixseifert.sanifill.analyser.model.SensorDataSma;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.constraint.NotNull;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MovingAverageCalculatorImpl implements MovingAverageCalculator {

    private final int numberOfValuesForMovingAverage = 2;

    private final Map<String, SensorData> mostRecentSensorData = new HashMap<>();

    private final Map<String, MovingAverageStatistics> movingAverageMap = new HashMap<>();

//    @PostConstruct
//    public void loadRecentSensorData() {
//        // TODO: load sensor data from storage
//    }

    @Override
    @ConsumeEvent("calculateSmaOfFillingGradient")
    @Blocking
    public SensorDataSma calculateSmaOfFillingGradient(@NotNull SensorData newSensorData) {
        addNewGradientToStatistics(newSensorData.getSensorId(), newSensorData);
        if(!movingAverageMap.get(newSensorData.getSensorId()).hasEnoughValuesForMovingAverage()) {
            return getEmptySma(newSensorData);
        }
        return getNewSma(newSensorData);
    }

    private SensorDataSma getEmptySma(SensorData newSensorData) {
        return new SensorDataSma(newSensorData.getSensorId(), newSensorData.getDateTime(), null);
    }

    private SensorDataSma getNewSma(SensorData newSensorData) {
        return new SensorDataSma(newSensorData.getSensorId(),
                newSensorData.getDateTime(),
                movingAverageMap.get(newSensorData.getSensorId()).getMean());
    }

    private void addNewGradientToStatistics(String sensorId, SensorData newSensorData) {
        if(sensorIdHasNoMovingAverageObjectAssigned(sensorId)) {
            movingAverageMap.put(sensorId, new MovingAverageStatistics(numberOfValuesForMovingAverage));
        }
        if(newValueCanBeCalculated(sensorId) && calculateGradient(sensorId, newSensorData) < 0) {
            movingAverageMap.get(sensorId).addValue(calculateGradient(sensorId, newSensorData));
        }
        mostRecentSensorData.put(sensorId, newSensorData);
    }

    private boolean sensorIdHasNoMovingAverageObjectAssigned(String sensorId) {
        return !movingAverageMap.containsKey(sensorId);
    }

    private boolean newValueCanBeCalculated(String sensorId) {
        return mostRecentSensorData.get(sensorId) != null;
    }

    private double calculateGradient(String sensorId, SensorData newSensorData) {
        return 0 - (mostRecentSensorData.get(sensorId).getData() - newSensorData.getData()) /
                (Duration.between(
                        mostRecentSensorData.get(sensorId).getDateTime(),
                        newSensorData.getDateTime())
                        .toSeconds());
    }
}
