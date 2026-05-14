package roomescape.dao.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.reservation.theme.Theme;
import roomescape.domain.reservation.theme.ThemeName;

public final class ThemeMapper {

    public static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) -> {
        return new Theme(
                rs.getLong("id"),
                ThemeName.parse(rs.getString("name")),
                rs.getString("description"),
                rs.getString("url")
        );
    };

    private ThemeMapper() {
    }
}
