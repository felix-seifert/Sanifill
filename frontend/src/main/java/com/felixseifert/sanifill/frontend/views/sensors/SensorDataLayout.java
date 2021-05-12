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
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;
import com.vaadin.flow.component.textfield.TextField;

import java.time.format.DateTimeFormatter;

public class SensorDataLayout extends VerticalLayout {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final SensorService sensorService;

    public SensorDataLayout(SensorDataEnriched data, SensorService sensorService) {
        this.sensorService = sensorService;

        addClassName("sensor-data-layout");
        add(createHeader(data),
                createProgressBarHorizontalLine(data));
    }

    private HorizontalLayout createHeader(SensorDataEnriched data) {
        H3 id = new H3(data.getSensorId());
        id.setWidthFull();
        Span dateTime = new Span("Last update: " + data.getDateTime().format(dateTimeFormatter));
        dateTime.addClassName("update-date-time");

        HorizontalLayout layout = new HorizontalLayout(id, dateTime);
        layout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        layout.setWidthFull();

        return layout;
    }

    private HorizontalLayout createProgressBarHorizontalLine(SensorDataEnriched data) {
        Div progressBar = createProgressBarWithLabel(data);
        Component expectedDepletionField = createDepletionField(data);
        Button refillButton = createRefillButton(data);

        HorizontalLayout fieldButtonLayout = new HorizontalLayout(expectedDepletionField, refillButton);
        fieldButtonLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        HorizontalLayout layout = new HorizontalLayout(progressBar, fieldButtonLayout);
        layout.setWidthFull();
        layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        return layout;
    }

    private Component createDepletionField(SensorDataEnriched data) {
        TextField timeField = new TextField("Expected Depletion");
        timeField.setValue(data.getExpectedDepletion().format(dateTimeFormatter));
        timeField.setHelperText("Based on SMA of linear gradient");
        timeField.setReadOnly(true);
        return timeField;
    }

    private Button createRefillButton(SensorDataEnriched data) {
        Button refillButton = new Button("Refill");
        refillButton.addClickListener(e -> triggerRefillOfSensor(data));
        return refillButton;
    }

    private Div createProgressBarWithLabel(SensorDataEnriched data) {
        FlexLayout label = createLabel(data);
        ProgressBar progressBar = createProgressBar(data);

        Div layout = new Div(label, progressBar);
        layout.setWidthFull();

        return layout;
    }

    private FlexLayout createLabel(SensorDataEnriched data) {
        Div labelText = new Div();
        labelText.setText("Current Filling");

        Div labelValue = new Div();
        labelValue.setText(transformToPercentage(data.getData()) + " %");

        FlexLayout label = new FlexLayout(labelText, labelValue);
        label.setJustifyContentMode(JustifyContentMode.BETWEEN);

        return label;
    }

    private ProgressBar createProgressBar(SensorDataEnriched data) {
        double min = 0.0, max = 1.0;
        ProgressBar progressBar = new ProgressBar(min, max);
        progressBar.setValue(data.getData());
        progressBar.addThemeVariants(getAppropriateProgressBarTheme(data));
        progressBar.setWidthFull();
        return progressBar;
    }

    private ProgressBarVariant getAppropriateProgressBarTheme(SensorDataEnriched data) {
        if(data.getData() >= 0.9) {
            return ProgressBarVariant.LUMO_SUCCESS;
        }
        if(data.getData() < 0.2) {
            return ProgressBarVariant.LUMO_ERROR;
        }
        return ProgressBarVariant.LUMO_CONTRAST;
    }

    private int transformToPercentage(double decimalNumber) {
        return (int) Math.round(100 * decimalNumber);
    }

    private void triggerRefillOfSensor(SensorDataEnriched data) {
         sensorService.triggerSensorReset(data);
    }
}
