package roomescape.theme.dao.mapper;

import org.h2.tools.SimpleResultSet;
import org.junit.jupiter.api.Test;
import roomescape.theme.domain.Theme;

import java.sql.SQLException;
import java.sql.Types;

import static org.assertj.core.api.Assertions.assertThat;

class ThemeRowMapperTest {
    private final ThemeRowMapper themeRowMapper = new ThemeRowMapper();

    @Test
    void mapRow() throws SQLException {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("id", Types.BIGINT, 10, 0);
        rs.addColumn("name", Types.VARCHAR, 255, 0);
        rs.addColumn("description", Types.VARCHAR, 255, 0);
        rs.addColumn("thumbnail", Types.VARCHAR, 255, 0);
        rs.addRow(1, "hotea", "nice", "very nice");
        rs.next();

        Theme theme = new Theme(1L, "hotea", "nice", "very nice");
        assertThat(themeRowMapper.mapRow(rs, 1)).isEqualTo(theme);
    }
}