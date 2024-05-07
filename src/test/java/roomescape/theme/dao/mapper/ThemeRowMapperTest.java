package roomescape.theme.dao.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.SQLException;
import java.sql.Types;

import org.h2.tools.SimpleResultSet;
import org.junit.jupiter.api.Test;

import roomescape.theme.domain.Theme;

class ThemeRowMapperTest {
    private final ThemeRowMapper themeRowMapper = new ThemeRowMapper();

    @Test
    void mapRow() throws SQLException {
        SimpleResultSet rs = new SimpleResultSet();
        rs.addColumn("id", Types.BIGINT, 10, 0);
        rs.addColumn("name", Types.VARCHAR, 255, 0);
        rs.addColumn("description", Types.VARCHAR, 255, 0);
        rs.addColumn("thumbnail", Types.VARCHAR, 255, 0);
        rs.addRow(1, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        rs.next();

        Theme theme = Theme.createWithId(1L, "정글 모험", "열대 정글의 심연을 탐험하세요.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        assertThat(themeRowMapper.mapRow(rs, 1)).isEqualTo(theme);
    }
}
