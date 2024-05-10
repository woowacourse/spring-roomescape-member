package roomescape.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import roomescape.domain.roomescape.Theme;
import roomescape.domain.roomescape.ThemeName;

@Component
public class ThemeRowMapper implements RowMapper<Theme> {
    @Override
    public Theme mapRow(final ResultSet resultSet, final int rowNumber) {
        try {
            return new Theme(
                    resultSet.getLong("theme_id"),
                    new ThemeName(resultSet.getString("theme_name")),
                    resultSet.getString("description"),
                    resultSet.getString("thumbnail")
            );
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
