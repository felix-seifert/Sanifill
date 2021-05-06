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

import com.felixseifert.sanifill.frontend.model.SensorData;
import com.felixseifert.sanifill.frontend.service.SensorService;
import com.felixseifert.sanifill.frontend.views.main.MainView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

import java.util.Map;

@Route(value = "sensors", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Sensors")
@CssImport("./views/sensors/sensors-view.css")
@SpringComponent
@UIScope
@PreserveOnRefresh
public class SensorViewImpl extends SensorView {

    private final SensorService sensorService;

    private final SensorDataGrid grid = new SensorDataGrid();

    public SensorViewImpl(SensorService sensorService) {
        this.sensorService = sensorService;

        addClassName("sensors-view");
        setSizeFull();
        add(grid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        sensorService.register(this);
        updateSensorData(sensorService.getSensorsAndTheirCurrentValue());
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        sensorService.unregister(this);
    }

    @Override
    public void updateSensorData(SensorData sensorData) {
        grid.addOrUpdateItem(sensorData);
    }

    @Override
    public void updateSensorData(Map<String, SensorData> sensorDataMap) {
        sensorDataMap.values().forEach(grid::addOrUpdateItem);
    }
}
