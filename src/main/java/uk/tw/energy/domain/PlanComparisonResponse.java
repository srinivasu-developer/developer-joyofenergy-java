package uk.tw.energy.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: srinivasun
 * @Since: 05/11/24
 */
public record PlanComparisonResponse(String pricePlanId, List<PlanConsumption> pricePlanComparisons) {

    public record PlanConsumption(String planName, BigDecimal consumption) {}
}
