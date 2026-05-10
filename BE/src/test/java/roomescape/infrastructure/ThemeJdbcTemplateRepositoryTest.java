package roomescape.infrastructure;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.entity.Theme;
import roomescape.entity.ThemeSortType;

@JdbcTest
@Import(ThemeJdbcTemplateRepository.class)
class ThemeJdbcTemplateRepositoryTest {

    private static final String TEST_THEMA_NAME = "테스트 테마";
    private static final String TEST_THEMA_DESCRIPTION = "테스트 테마 설명";
    private static final String TEST_THEMA_THUMBNAIL = "https://good.com/thumb-nail";

    private final JdbcTemplate jdbcTemplate;
    private final ThemeJdbcTemplateRepository themeRepository;

    @Autowired
    public ThemeJdbcTemplateRepositoryTest(ThemeJdbcTemplateRepository themeRepository, JdbcTemplate jdbcTemplate) {
        this.themeRepository = themeRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    @DisplayName("테마를 잘 저장한다")
    @Sql(scripts = "/sql/cleanup.sql")
    void save_success() {
        // given
        Theme theme = Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);

        // when
        Theme savedTheme = themeRepository.save(theme);

        // then
        Assertions.assertNotNull(savedTheme.id());

        Theme expect = theme.appendId(savedTheme.id());
        Assertions.assertEquals(expect, savedTheme);
    }

    @Test
    @DisplayName("아이디를 기반으로 테마를 찾는다")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/theme/find-by-id-fixtures.sql"
    })
    void findById_success() {
        // when
        Optional<Theme> foundTheme = themeRepository.findById(1L);

        // then
        Assertions.assertTrue(foundTheme.isPresent());
        Assertions.assertEquals(1L, foundTheme.get().id());
        Assertions.assertEquals(TEST_THEMA_NAME, foundTheme.get().name());
    }

    @Test
    @DisplayName("아이디를 기반으로 테마를 찾는다 - 없어도 오류가 발생하지 않는다. - Optional<Empty> 반환")
    @Sql(scripts = "/sql/cleanup.sql")
    void findById_success_even_if_no_theme() {
        // when
        Optional<Theme> foundTheme = themeRepository.findById(999L);

        // then
        assertTrue(foundTheme.isEmpty());
    }

    @Test
    @DisplayName("모든 테마를 가져온다")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/theme/find-all-fixtures.sql"
    })
    void findAll_success() {
        // when
        List<Theme> result = themeRepository.findAll();

        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("모든 테마를 가져온다 - 없는 경우에는 빈 리스트 반환")
    @Sql(scripts = "/sql/cleanup.sql")
    void findAll_success_even_if_no_theme() {
        // when
        List<Theme> result = themeRepository.findAll();

        // then
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("아이디를 기반으로 테마를 삭제한다")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/theme/delete-fixtures.sql"
    })
    void deleteById_success() {
        // when
        themeRepository.deleteById(1L);

        // then
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM theme WHERE id = 1", Integer.class);
        Assertions.assertEquals(0, count);
    }

    @Test
    @DisplayName("없는 ID 기반으로 테마를 삭제해도 오류가 발생하지 않는다")
    @Sql(scripts = "/sql/cleanup.sql")
    void deleteById_success_even_if_no_theme() {
        // when & then
        Assertions.assertDoesNotThrow(
                () -> themeRepository.deleteById(999L)
        );
    }

    @Test
    @DisplayName("기간 내 예약 수가 많은 테마를 상위 N개 조회한다")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/theme/find-top-n-fixtures.sql"
    })
    void findTopNByPeriod_success() {
        // when
        List<Theme> result = themeRepository.findTopNByPeriod(
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 7),
                ThemeSortType.POPULAR,
                2L
        );

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).id());
        Assertions.assertEquals(2L, result.get(1).id());
    }
}
