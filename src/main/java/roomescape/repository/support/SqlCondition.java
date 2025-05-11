package roomescape.repository.support;

import java.util.List;

public record SqlCondition(String clause, Object[] params) {

    public static SqlCondition createConditionClause(final List<String> conditions, final Object... params) {
        if (conditions.isEmpty()) {
            return new SqlCondition("", new Object[]{});
        }
        final String clause = String.join(" AND ", conditions);
        return new SqlCondition(clause, params);
    }

    public static <T> void addCondition(List<String> conditions, List<Object> params, String sql, T value) {
        if (value != null) {
            conditions.add(sql);
            params.add(value);
        }
    }
}
