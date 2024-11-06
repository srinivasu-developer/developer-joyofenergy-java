package uk.tw.energy.controller.v2;

import static uk.tw.energy.domain.constants.ApiConstants.PRICE_PLAN_API_V2;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.tw.energy.adapters.PricePlanAdapter;
import uk.tw.energy.domain.PlanComparisonResponse;
import uk.tw.energy.exceptions.RecordNotFoundException;
import uk.tw.energy.service.AccountService;
import uk.tw.energy.service.PricePlanService;

/**
 * @Author: srinivasun
 * @Since: 05/11/24
 */
@RequestMapping(PRICE_PLAN_API_V2)
@RestController
public class PricePlanComparatorControllerV2 {

    private final AccountService accountService;
    private final PricePlanService pricePlanService;

    @Autowired
    public PricePlanComparatorControllerV2(AccountService accountService, PricePlanService pricePlanService) {
        this.accountService = accountService;
        this.pricePlanService = pricePlanService;
    }

    /**
     * To compare price plans; gives all the consumption cost of smart meter readings for each price plan
     *
     * @param smartMeterId a smart meter ID to fetch readings and compare plans
     * @return a list of price plan comparisons and current price-plan name
     */
    @GetMapping(value = "/compare")
    public ResponseEntity<PlanComparisonResponse> getPricePlanComparisons(@PathVariable String smartMeterId) {
        String pricePlanId = accountService.getPricePlanIdForSmartMeterId(smartMeterId);

        Map<String, BigDecimal> planComparisons = pricePlanService
                .getConsumptionCostOfElectricityReadingsForEachPricePlan(smartMeterId)
                .orElseThrow(() -> new RecordNotFoundException(smartMeterId));

        List<PlanComparisonResponse.PlanConsumption> consumptions = PricePlanAdapter.toPlanConsumption(planComparisons);

        return ResponseEntity.ok(new PlanComparisonResponse(pricePlanId, consumptions));
    }

    /**
     * Gives recommendations by showing all price-plan's consumption rates based on electricity readings of the provided
     * smart meter ID.
     *
     * @param smartMeterId a valid smart meter ID to fetch readings
     * @return a list of price plan comparisons ordered by consumption rates
     */
    @GetMapping(value = "/recommend")
    public ResponseEntity<List<PlanComparisonResponse.PlanConsumption>> getPricePlanRecommendation(
            @PathVariable String smartMeterId) {

        Map<String, BigDecimal> planComparisons = pricePlanService
                .getConsumptionCostOfElectricityReadingsForEachPricePlan(smartMeterId)
                .orElseThrow(() -> new RecordNotFoundException(smartMeterId));

        List<PlanComparisonResponse.PlanConsumption> consumptions = PricePlanAdapter.toPlanConsumption(planComparisons);
        consumptions.sort(Comparator.comparing(PlanComparisonResponse.PlanConsumption::consumption));

        return ResponseEntity.ok(consumptions);
    }
}
