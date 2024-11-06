package uk.tw.energy.controller.v2;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import uk.tw.energy.service.AccountService;
import uk.tw.energy.service.PricePlanService;

/**
 * @Author: srinivasun
 * @Since: 05/11/24
 */
@WebMvcTest(controllers = PricePlanComparatorControllerV2.class)
public class PricePlanComparatorV2Test {

    private static final String PRICE_PLANS_API = "/api/joe/v2/meters/{smartMeterId}/price-plans";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private PricePlanService pricePlanService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testComparePricePlans_Success() throws Exception {
        String meterId = "test-meter-id";
        String testPricePlanId = "test-price-plan";
        when(accountService.getPricePlanIdForSmartMeterId(meterId)).thenReturn(testPricePlanId);
        when(pricePlanService.getConsumptionCostOfElectricityReadingsForEachPricePlan(meterId))
                .thenReturn(Optional.of(new HashMap<>()));

        mockMvc.perform(get(PRICE_PLANS_API + "/compare", meterId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("pricePlanId").value(testPricePlanId));
    }

    @Test
    public void testComparePricePlans_ResourceNotFound() throws Exception {
        String meterId = "test-meter-id";
        String testPricePlanId = "test-price-plan";
        when(accountService.getPricePlanIdForSmartMeterId(meterId)).thenReturn(testPricePlanId);

        mockMvc.perform(get(PRICE_PLANS_API + "/compare", meterId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testRecommendPricePlans_Success() throws Exception {
        String meterId = "test-meter-id";

        Map<String, BigDecimal> planConsumptions = Map.of(
                "plan-1",
                BigDecimal.valueOf(10.12),
                "plan-2",
                BigDecimal.valueOf(7.12),
                "plan-3",
                BigDecimal.valueOf(7.13),
                "plan-4",
                BigDecimal.valueOf(7.13));

        when(pricePlanService.getConsumptionCostOfElectricityReadingsForEachPricePlan(meterId))
                .thenReturn(Optional.of(planConsumptions));

        mockMvc.perform(get(PRICE_PLANS_API + "/recommend", meterId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
