package roomescape.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.Theme;

@Component
public class ThemeRowMapper implements RowMapper<Theme> {

    @Override
    public Theme mapRow(ResultSet rs, int rowNum) throws SQLException {
        long themeId = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String thumbnail = rs.getString("thumbnail");
        return Theme.of(themeId, name, description, thumbnail);
    }
}
