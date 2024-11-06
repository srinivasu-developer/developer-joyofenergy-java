package uk.tw.energy.adapters;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import uk.tw.energy.domain.PlanComparisonResponse;

/**
 * @Author: srinivasun
 * @Since: 05/11/24
 */
public class PricePlanAdapter {

    public static List<PlanComparisonResponse.PlanConsumption> toPlanConsumption(Map<String, BigDecimal> consumption) {
        return consumption.entrySet().stream()
                .map(entry -> new PlanComparisonResponse.PlanConsumption(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
