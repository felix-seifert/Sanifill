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

package com.felixseifert.sanifill.frontend.views.sensors;

import com.felixseifert.sanifill.frontend.model.SensorDataEnriched;
import com.felixseifert.sanifill.frontend.service.SensorService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;

import java.util.HashMap;
import java.util.Map;

public class SensorDataGrid extends Grid<SensorDataEnriched> {

    private final Map<String, SensorDataEnriched> currentSensorData = new HashMap<>();

    public SensorDataGrid(SensorService sensorService) {
        this.setHeight("100%");
        this.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        this.addComponentColumn(data -> new SensorDataLayout(data, sensorService));
        this.setItems(currentSensorData.values());
    }

    public void addOrUpdateItem(SensorDataEnriched data) {
            currentSensorData.put(data.getSensorId(), data);
            this.getDataProvider().refreshAll();
    }
}
