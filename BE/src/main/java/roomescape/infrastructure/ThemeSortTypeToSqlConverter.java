package roomescape.infrastructure;

import java.util.HashMap;
import java.util.Map;
import roomescape.entity.ThemeSortType;

public class ThemeSortTypeToSqlConverter {
    public static Map<ThemeSortType, String> SQL_EXPRESSIONS = new HashMap<>();

    public static String convert(ThemeSortType type) {
        return SQL_EXPRESSIONS.get(type);
    }
}
