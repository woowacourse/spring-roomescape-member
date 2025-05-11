package roomescape.reservation.infrastructure;

import java.util.LinkedHashMap;
import java.util.Map;

public class SqlBuilder {
    private final StringBuilder sql;
    private final Map<String, Object> params;

    public SqlBuilder(String baseSql) {
        this.sql = new StringBuilder(baseSql);
        this.params = new LinkedHashMap<>();
    }

    public SqlBuilder and(String clause, String paramName, Object value) {
        if (value != null) {
            sql.append(" AND ").append(clause);
            params.put(paramName, value);
        }
        return this;
    }

    public String getSql() {
        return sql.toString();
    }

    public Map<String, Object> getParams() {
        return params;
    }
}
