package uk.tw.energy.domain;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record MeterReadings(

        String smartMeterId,

        @NotEmpty(message = "Mandatory with at least one reading")
        List<ElectricityReading> electricityReadings) {

}
