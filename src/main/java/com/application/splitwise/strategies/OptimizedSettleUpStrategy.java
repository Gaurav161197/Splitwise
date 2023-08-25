package com.application.splitwise.strategies;

import com.application.splitwise.models.Member;
import com.application.splitwise.models.SettleUpSummaryResource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
;import java.util.*;


@Component("optimizedSettleUpStrategy")
@Primary
public class OptimizedSettleUpStrategy implements SettleUpStrategy {

    @Override
    public List<SettleUpSummaryResource> checkSettleUpSummary(Map<Member, Double> currentExpenseStatusMap) {
        int expenseSummaryCount = 0;
        List<SettleUpSummaryResource> settleUpSummaryResourceList = new ArrayList<>();
        PriorityQueue<CustomMemberAmountObj> membersQueueToReceive = new PriorityQueue<>(new CustomComparatorMaxValue());
        PriorityQueue<CustomMemberAmountObj> membersQueueToPay = new PriorityQueue<>(new CustomComparatorMinValue());

        for (Member member : currentExpenseStatusMap.keySet()) {
            double amount = currentExpenseStatusMap.get(member);
            CustomMemberAmountObj customMemberAmountObj = new CustomMemberAmountObj(member, amount);
            if (currentExpenseStatusMap.get(member) > 0)
                membersQueueToReceive.add(customMemberAmountObj);
            else if (currentExpenseStatusMap.get(member) < 0)
                membersQueueToPay.add(customMemberAmountObj);
        }

        while (!membersQueueToReceive.isEmpty() && !membersQueueToPay.isEmpty()) {
            CustomMemberAmountObj maxToReceive = membersQueueToReceive.peek();
            CustomMemberAmountObj maxToPay = membersQueueToPay.peek();
            double maxToReceiveAmount = maxToReceive.getAmount();
            double maxToPayAmount = maxToPay.getAmount();

            SettleUpSummaryResource settleUpSummaryResource = new SettleUpSummaryResource(++expenseSummaryCount);
            if (maxToReceiveAmount < Math.abs(maxToPayAmount)) {
                membersQueueToPay.add(new CustomMemberAmountObj(maxToPay.getMember(), maxToPayAmount + maxToReceiveAmount));
                settleUpSummaryResource.setAmount(maxToReceiveAmount);

            } else if (maxToReceiveAmount > Math.abs(maxToPayAmount)) {
                settleUpSummaryResource.setAmount(Math.abs(maxToPayAmount));
                membersQueueToReceive.add(new CustomMemberAmountObj(maxToReceive.getMember(), maxToReceiveAmount - Math.abs(maxToPayAmount)));

            } else if (maxToReceiveAmount == Math.abs(maxToPayAmount)) {
                settleUpSummaryResource.setAmount(Math.abs(maxToPayAmount));
            }
            membersQueueToReceive.remove();
            membersQueueToPay.remove();
            settleUpSummaryResource.setPaidTo(maxToReceive.getMember());
            settleUpSummaryResource.setPaidBy(maxToPay.getMember());
            settleUpSummaryResourceList.add(settleUpSummaryResource);

        }
        printSummary(settleUpSummaryResourceList);
        return settleUpSummaryResourceList;

    }

    private void printSummary(List<SettleUpSummaryResource> settleUpSummaryResourceList) {
        System.out.println("Here is the optimized transactions that should happen, to settle up..!");
        for (SettleUpSummaryResource settleUpSummaryResource : settleUpSummaryResourceList) {
            Member payingMember = settleUpSummaryResource.getPaidBy();
            Member receivingMember = settleUpSummaryResource.getPaidTo();
            double amount = settleUpSummaryResource.getAmount();
            System.out.println(settleUpSummaryResource.getId() + ". " + payingMember.getName() + " will pay " + receivingMember.getName() + " Rs. " + Math.abs(amount));
        }
    }


}

class CustomComparatorMinValue implements Comparator<CustomMemberAmountObj> {

    @Override
    public int compare(CustomMemberAmountObj m1, CustomMemberAmountObj m2) {
        return (int) Math.ceil((m1.getAmount() - m2.getAmount()));
    }
}

class CustomComparatorMaxValue implements Comparator<CustomMemberAmountObj> {

    @Override
    public int compare(CustomMemberAmountObj m1, CustomMemberAmountObj m2) {
        return (int) Math.ceil((m2.getAmount() - m1.getAmount()));
    }
}


@Data
@Setter
@AllArgsConstructor
class CustomMemberAmountObj {
    Member member;
    double Amount;
}
