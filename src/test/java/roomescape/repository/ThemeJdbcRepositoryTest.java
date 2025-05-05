package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.Fixtures.JUNK_THEME;
import static roomescape.Fixtures.JUNK_THEME_1;
import static roomescape.Fixtures.JUNK_THEME_2;
import static roomescape.Fixtures.JUNK_THEME_3;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.model.Theme;

@Sql(scripts = {"/test-schema.sql", "/test-data.sql"})
@JdbcTest
class ThemeJdbcRepositoryTest {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeJdbcRepositoryTest(DataSource dataSource) {
        this.themeRepository = new ThemeJdbcRepository(dataSource);
    }

    @Test
    @DisplayName("모든 테마 목록을 조회한다.")
    void findAll() {
        // given
        var themes = List.of(
                JUNK_THEME_1, JUNK_THEME_2, JUNK_THEME_3
        );

        // when
        final List<Theme> found = themeRepository.findAll();

        // then
        assertThat(found).containsExactlyInAnyOrderElementsOf(themes);

    }

    @Test
    @DisplayName("테마를 저장한다.")
    void save() {
        // given
        var theme = JUNK_THEME;

        // when
        final Long saved = themeRepository.save(theme);

        // then
        assertThat(saved).isEqualTo(theme.id());
    }

    @Test
    @DisplayName("테마 ID에 해당하는 테마를 조회한다.")
    void findById() {
        // given
        var theme = JUNK_THEME_1;
        var themeId = theme.id();

        // when
        final Optional<Theme> found = themeRepository.findById(themeId);

        // then
        assertThat(found).isPresent()
                .isEqualTo(Optional.of(theme));
    }

    @Test
    @DisplayName("테마 ID에 해당하는 테마를 삭제한다.")
    void removeById() {
        // given
        var theme = JUNK_THEME;
        themeRepository.save(theme);
        var themeId = theme.id();

        // when
        final Boolean removed = themeRepository.removeById(themeId);

        // then
        assertThat(removed).isTrue();
    }
}
