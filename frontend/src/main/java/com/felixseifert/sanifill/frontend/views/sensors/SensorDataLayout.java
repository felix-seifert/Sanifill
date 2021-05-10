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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;

public class SensorDataLayout extends VerticalLayout {

    private final SensorService sensorService;

    public SensorDataLayout(SensorData sensorData, SensorService sensorService) {
        this.sensorService = sensorService;
        this.add(createHeader(sensorData),
                createProgressBarAndRefillButton(sensorData));
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

    private HorizontalLayout createProgressBarAndRefillButton(SensorData sensorData) {
        Div progressBar = createProgressBarWithLabel(sensorData);
        Button refillButton = createRefillButton(sensorData);

        HorizontalLayout layout = new HorizontalLayout(progressBar, refillButton);
        layout.setWidthFull();
        layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        return layout;
    }

    private Button createRefillButton(SensorData sensorData) {
        Button refillButton = new Button("Refill");
        refillButton.addClickListener(e -> triggerRefillOfSensor(sensorData));
        return refillButton;
    }

    private Div createProgressBarWithLabel(SensorData sensorData) {
        FlexLayout label = createLabel(sensorData);
        ProgressBar progressBar = createProgressBar(sensorData);

        Div layout = new Div(label, progressBar);
        layout.setWidthFull();

        return layout;
    }

    private FlexLayout createLabel(SensorData sensorData) {
        Div labelText = new Div();
        labelText.setText("Current Filling");

        Div labelValue = new Div();
        labelValue.setText(transformToPercentage(sensorData.getData()) + " %");

        FlexLayout label = new FlexLayout(labelText, labelValue);
        label.setJustifyContentMode(JustifyContentMode.BETWEEN);

        return label;
    }

    private ProgressBar createProgressBar(SensorData sensorData) {
        double min = 0.0, max = 1.0;
        ProgressBar progressBar = new ProgressBar(min, max);
        progressBar.setValue(sensorData.getData());
        progressBar.addThemeVariants(getAppropriateProgressBarTheme(sensorData));
        progressBar.setWidthFull();
        return progressBar;
    }

    private ProgressBarVariant getAppropriateProgressBarTheme(SensorData sensorData) {
        if(sensorData.getData() >= 0.9) {
            return ProgressBarVariant.LUMO_SUCCESS;
        }
        if(sensorData.getData() < 0.2) {
            return ProgressBarVariant.LUMO_ERROR;
        }
        return ProgressBarVariant.LUMO_CONTRAST;
    }

    private int transformToPercentage(double decimalNumber) {
        return (int) Math.round(100 * decimalNumber);
    }

    private void triggerRefillOfSensor(SensorData sensorData) {
         sensorService.triggerSensorReset(sensorData);
    }
}
