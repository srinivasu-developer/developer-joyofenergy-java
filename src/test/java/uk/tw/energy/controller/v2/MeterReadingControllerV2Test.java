package uk.tw.energy.controller.v2;

import static org.mockito.Mockito.when;
import static uk.tw.energy.domain.constants.ApiConstants.JSON_CONTENT_TYPE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.tw.energy.builders.MeterReadingsBuilder;
import uk.tw.energy.domain.MeterReadings;
import uk.tw.energy.service.MeterReadingService;

/**
 * @Author: srinivasun
 * @Since: 05/11/24
 */
@WebMvcTest(controllers = MeterReadingControllerV2.class)
public class MeterReadingControllerV2Test {

    private static final String READINGS_API = "/api/joe/v2/meters/{meterId}/readings";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MeterReadingService meterReadingService;

    @Test
    public void testCreateReading_Success() throws Exception {

        String meterID = "test-meter";

        MeterReadings readings =
                new MeterReadingsBuilder().generateElectricityReadings().build();

        mockMvc.perform(MockMvcRequestBuilders.post(READINGS_API, meterID)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(readings)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Mockito.verify(meterReadingService, Mockito.times(1)).storeReadings(meterID, readings.electricityReadings());
    }

    @Test
    public void testCreateReading_BadRequest_EmptyReadings() throws Exception {

        String meterID = "test-meter";

        MeterReadings readings = new MeterReadingsBuilder().build();

        mockMvc.perform(MockMvcRequestBuilders.post(READINGS_API, meterID)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(readings)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(meterReadingService, Mockito.times(0)).storeReadings(meterID, readings.electricityReadings());
    }

    @Test
    public void testGetReadings_Success() throws Exception {

        String meterID = "test-meter";

        MeterReadings mockMeterReadings =
                new MeterReadingsBuilder().generateElectricityReadings().build();

        when(meterReadingService.getReadings(meterID))
                .thenReturn(Optional.ofNullable(mockMeterReadings.electricityReadings()));

        mockMvc.perform(MockMvcRequestBuilders.get(READINGS_API, meterID))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(meterReadingService, Mockito.times(1)).getReadings(meterID);
    }

    @Test
    public void testGetReadings_NoRecordFound() throws Exception {

        String meterID = "test-meter";

        MeterReadings mockMeterReadings =
                new MeterReadingsBuilder().generateElectricityReadings().build();

        when(meterReadingService.getReadings("wrong-id"))
                .thenReturn(Optional.ofNullable(mockMeterReadings.electricityReadings()));

        mockMvc.perform(MockMvcRequestBuilders.get(READINGS_API, meterID))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.verify(meterReadingService, Mockito.times(1)).getReadings(meterID);
    }

    @Test
    public void testGetReadings_NoContent() throws Exception {

        String meterID = "test-meter";

        MeterReadings mockMeterReadings = new MeterReadingsBuilder().build();

        when(meterReadingService.getReadings(meterID))
                .thenReturn(Optional.ofNullable(mockMeterReadings.electricityReadings()));

        mockMvc.perform(MockMvcRequestBuilders.get(READINGS_API, meterID))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(meterReadingService, Mockito.times(1)).getReadings(meterID);
    }
}
