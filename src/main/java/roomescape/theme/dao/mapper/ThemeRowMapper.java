package roomescape.theme.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import roomescape.theme.domain.Theme;

@Component
public class ThemeRowMapper implements RowMapper<Theme> {
    @Override
    public Theme mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Theme.createWithId(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("thumbnail")
        );
    }
}
