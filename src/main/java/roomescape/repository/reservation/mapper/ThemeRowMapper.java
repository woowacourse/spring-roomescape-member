package roomescape.repository.reservation.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.reservation.Theme;

public class ThemeRowMapper implements RowMapper<Theme> {

    public static final ThemeRowMapper INSTANCE = new ThemeRowMapper();

    private ThemeRowMapper() {
    }

    @Override
    public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Theme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("thumbnail")
        );
    }
}
