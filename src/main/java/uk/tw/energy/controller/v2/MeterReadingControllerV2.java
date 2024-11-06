package uk.tw.energy.controller.v2;

import static uk.tw.energy.domain.constants.ApiConstants.READINGS_API_V2;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.MeterReadings;
import uk.tw.energy.exceptions.RecordNotFoundException;
import uk.tw.energy.service.MeterReadingService;

/**
 * @Author: srinivasun
 * @Since: 05/11/24
 */
@RequestMapping(READINGS_API_V2)
@RestController
public class MeterReadingControllerV2 {

    private final MeterReadingService meterReadingService;

    @Autowired
    public MeterReadingControllerV2(MeterReadingService meterReadingService) {
        this.meterReadingService = meterReadingService;
    }

    /**
     * Creates meter readings and add them to the smart meter with provided ID
     *
     * @param smartMeterId a smart reader ID to attach readings
     * @param meterReadings a non-empty list of meter readings to create and store with the meter id
     * @return a string in success, otherwise appropriate HttpStatus
     */
    @ResponseStatus(code = HttpStatus.CREATED)
    @PostMapping
    public String storeMeterReadings(
            @PathVariable String smartMeterId, @RequestBody @Valid MeterReadings meterReadings) {
        meterReadingService.storeReadings(smartMeterId, meterReadings.electricityReadings());
        return "STORED";
    }

    /**
     * Gets meter readings for the smart reader ID
     *
     * @param smartMeterId a smart reader ID to attach readings
     * @return a list of electricity readings associated with the meter
     */
    @GetMapping
    public ResponseEntity<List<ElectricityReading>> findMeterReadings(@PathVariable String smartMeterId) {
        List<ElectricityReading> readings = meterReadingService
                .getReadings(smartMeterId)
                .orElseThrow(() -> new RecordNotFoundException(smartMeterId));
        if (readings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(readings);
    }
}
