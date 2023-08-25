package DTOs;
import com.application.splitwise.models.Member;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AddExpenseRequestResource {
    private long groupId;
    private double amount;
    private Map<Member,Double> amountPaidByUser;
    private List<Member> usersInvolved;
}
