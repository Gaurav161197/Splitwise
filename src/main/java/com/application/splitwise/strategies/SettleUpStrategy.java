package com.application.splitwise.strategies;

import com.application.splitwise.models.Member;
import com.application.splitwise.models.SettleUpSummaryResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface SettleUpStrategy {
   List<SettleUpSummaryResource> checkSettleUpSummary(Map<Member, Double> currentExpenseStatusMap);
}
