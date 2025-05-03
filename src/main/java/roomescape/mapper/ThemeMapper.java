package roomescape.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.Theme;

public class ThemeMapper implements RowMapper<Theme> {

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
