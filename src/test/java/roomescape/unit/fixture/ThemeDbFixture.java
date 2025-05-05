package roomescape.unit.fixture;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeThumbnail;

@Component
public class ThemeDbFixture {

    private JdbcTemplate jdbcTemplate;

    public ThemeDbFixture(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme 공포() {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        String name = "공포";
        String description = "공포 테마";
        String thumbnail = "공포.jpg";

        Long id = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("description", description)
                .addValue("thumbnail", thumbnail)
        ).longValue();

        return new Theme(
                id,
                new ThemeName(name),
                new ThemeDescription(description),
                new ThemeThumbnail(thumbnail)
        );
    }

    public Theme 로맨스() {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        String name = "로멘스";
        String description = "로멘스 테마";
        String thumbnail = "로멘스.jpg";

        Long id = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("description", description)
                .addValue("thumbnail", thumbnail)
        ).longValue();

        return new Theme(
                id,
                new ThemeName(name),
                new ThemeDescription(description),
                new ThemeThumbnail(thumbnail)
        );    }

    public Theme 커스텀_테마(String customName) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("theme")
                .usingGeneratedKeyColumns("id");

        String name = customName;
        String description = customName + "테마";
        String thumbnail = customName + ".jpg";

        Long id = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("description", description)
                .addValue("thumbnail", thumbnail)
        ).longValue();

        return new Theme(
                id,
                new ThemeName(name),
                new ThemeDescription(description),
                new ThemeThumbnail(thumbnail)
        );    }
}
