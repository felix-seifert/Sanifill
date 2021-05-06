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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

public class SensorDataLayout extends VerticalLayout {

    private final SensorService sensorService;

    public SensorDataLayout(SensorData sensorData, SensorService sensorService) {
        this.sensorService = sensorService;
        this.add(createHeader(sensorData),
                createProgressBarWithRefillButton(sensorData));
    }

    private HorizontalLayout createHeader(SensorData sensorData) {
        H3 id = new H3(sensorData.getSensorId());
        id.setWidthFull();
        Span dateTime = new Span(sensorData.getDateTime().toString());

        HorizontalLayout layout = new HorizontalLayout(id, dateTime);
        layout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        layout.setWidthFull();

        return layout;
    }

    private HorizontalLayout createProgressBarWithRefillButton(SensorData sensorData) {
        double min = 0.0, max = 1.0;
        ProgressBar progressBar = new ProgressBar(min, max);
        progressBar.setValue(sensorData.getData());
        // TODO: .addThemeVariants(ProgressBarVariant.LUMO_ERROR) if filling below 20 %
        //  (LUMO_SUCCESS for fresh filling of 90 % and above)
        // TODO: Consider to show value of filling
        progressBar.setWidthFull();

        Button refillButton = new Button("Refill");
        refillButton.addClickListener(e -> triggerRefillOfSensor(sensorData));

        HorizontalLayout layout = new HorizontalLayout(progressBar, refillButton);
        layout.setWidthFull();

        return layout;
    }

    private void triggerRefillOfSensor(SensorData sensorData) {
         sensorService.triggerSensorReset(sensorData);
    }
}
