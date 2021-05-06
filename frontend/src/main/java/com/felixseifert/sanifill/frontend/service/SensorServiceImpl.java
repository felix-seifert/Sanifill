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
import com.felixseifert.sanifill.frontend.views.sensors.SensorView;
import com.vaadin.flow.component.UI;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SensorServiceImpl implements SensorService {

    @Getter
    private final Map<UI, SensorView> sensorViews = new HashMap<>();

    @Getter
    private final Map<String, SensorData> sensorsAndTheirCurrentValue = new HashMap<>();

    @Override
    public void sendSensorDataToUis(SensorData sensorData) {
        sensorViews.keySet().forEach(ui -> ui.access(() -> sensorViews.get(ui).addNewSensorDataToGrid(sensorData)));
        // TODO: Add new sensor data OR update sensor data for existing sensors
        // TODO: Get current sensor data from memory if new page access (do not wait for sensor updates)
        sensorsAndTheirCurrentValue.put(sensorData.getSensorId(), sensorData);
        sensorViews.keySet().forEach(ui -> ui.access(() -> sensorViews.get(ui).updateSensorData(sensorData)));
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
