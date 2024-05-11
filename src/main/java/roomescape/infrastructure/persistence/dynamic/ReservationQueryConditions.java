package roomescape.infrastructure.persistence.dynamic;

import java.util.List;

public class ReservationQueryConditions {

    private final List<Condition> conditions;
    private final Object[] args;
    private final int[] argTypes;

    ReservationQueryConditions(List<Condition> conditions, Object[] args, int[] argTypes) {
        this.conditions = conditions;
        this.args = args;
        this.argTypes = argTypes;
    }

    public static ReservationQueryConditionsBuilder builder() {
        return new ReservationQueryConditionsBuilder();
    }

    public static ReservationQueryConditions noneConditions() {
        return new ReservationQueryConditionsBuilder().build();
    }

    public String createDynamicQuery(String base) {
        if (conditions.size() == 0) {
            return base;
        }
        String query = createQuery(base);

        return query.trim();
    }

    private String createQuery(String base) {
        StringBuilder sb = new StringBuilder(base);
        sb.append(" WHERE");

        int conditionSize = conditions.size();
        while (conditionSize > 0) {
            Condition condition = conditions.get(conditions.size() - conditionSize);
            sb.append(" ");
            sb.append(condition.getCondition());
            conditionSize--;
            if (conditionSize >= 1) {
                sb.append(" AND");
            }
        }

        return sb.toString();
    }

    public Object[] getArgs() {
        return args;
    }

    public int[] getArgTypes() {
        return argTypes;
    }
}
