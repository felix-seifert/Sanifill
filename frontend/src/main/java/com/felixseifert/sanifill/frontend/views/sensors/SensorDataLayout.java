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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class SensorDataLayout extends VerticalLayout {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private final SensorService sensorService;

    public SensorDataLayout(SensorDataEnriched data, SensorService sensorService) {
        this.sensorService = sensorService;
        addClassName("sensor-data-layout");
        add(createHeader(data), createProgressBarHorizontalLine(data));
    }

    private HorizontalLayout createHeader(SensorDataEnriched data) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        layout.setWidthFull();
        layout.add(createIdHeading(data.getSensorId()));
        layout.add(createDataTimeSpan(data.getDateTime()));
        return layout;
    }

    private H3 createIdHeading(String id) {
        H3 idHeading = new H3(id);
        idHeading.setWidthFull();
        return idHeading;
    }

    private Span createDataTimeSpan(LocalDateTime dateTime) {
        Span span = new Span("Last update: " + dateTime.format(dateTimeFormatter));
        span.addClassName("update-date-time");
        return span;
    }

    private HorizontalLayout createProgressBarHorizontalLine(SensorDataEnriched data) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        if(!isStandardDataAvailable(data)) return layout;
        layout.add(createProgressBarWithLabel(data));
        layout.add(createDepletionFieldAndRefillButtonLayout(data));
        return layout;
    }

    private HorizontalLayout createDepletionFieldAndRefillButtonLayout(SensorDataEnriched data) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        if(isExpectedDepletionAvailable(data)) {
            layout.add(createDepletionField(data));
        }
        layout.add(createRefillButton(data));
        return layout;
    }

    private boolean isStandardDataAvailable(SensorDataEnriched data) {
        return !(Objects.isNull(data.getDateTime())
                && Objects.isNull(data.getSensorAddress())
                && Objects.isNull(data.getSensorPort())
                && Objects.isNull(data.getData()));
    }

    private boolean isExpectedDepletionAvailable(SensorDataEnriched data) {
        return !Objects.isNull(data.getExpectedDepletion());
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
        return combineLabelAndProgressbarInDiv(label, progressBar);
    }

    private Div combineLabelAndProgressbarInDiv(FlexLayout label, ProgressBar progressBar) {
        Div div = new Div(label, progressBar);
        div.setWidthFull();
        return div;
    }

    private FlexLayout createLabel(SensorDataEnriched data) {
        Div labelText = createDivWithText("Current Filling");
        Div labelValue = createDivWithText(transformToPercentage(data.getData()) + " %");
        return justifyTwoTexts(labelText, labelValue);
    }

    private FlexLayout justifyTwoTexts(Div text1, Div text2) {
        FlexLayout layout = new FlexLayout(text1, text2);
        layout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        return layout;
    }

    private Div createDivWithText(String text) {
        Div div = new Div();
        div.setText(text);
        return div;
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
