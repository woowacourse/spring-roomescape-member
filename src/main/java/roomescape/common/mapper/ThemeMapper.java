package roomescape.common.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import roomescape.domain.Theme;

public class ThemeMapper implements RowMapper<Theme> {
    @Override
    public Theme mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}
