package roomescape.infrastructure.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.Theme;
import roomescape.domain.ThemeName;

public class ThemeRowMapper implements RowMapper<Theme> {

    private static final ThemeRowMapper INSTANCE = new ThemeRowMapper();

    private ThemeRowMapper() {
    }

    public static ThemeRowMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Theme(
                rs.getLong("id"),
                new ThemeName(rs.getString("name")),
                rs.getString("description"),
                rs.getString("thumbnail")
        );
    }
}
