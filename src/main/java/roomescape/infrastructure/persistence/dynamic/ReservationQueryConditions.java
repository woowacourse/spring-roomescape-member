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

        return createQuery(base);
    }

    private String createQuery(String base) {
        StringBuilder sql = new StringBuilder(base);
        sql.append(" WHERE");

        for (Condition condition : conditions) {
            sql.append(String.format(" %s ", condition.getCondition()));
            sql.append("AND");
        }

        sql.setLength(sql.length() - "AND".length());
        return sql.toString().trim();
    }

    public Object[] getArgs() {
        return args;
    }

    public int[] getArgTypes() {
        return argTypes;
    }
}
