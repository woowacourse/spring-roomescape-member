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
    void findById_success() {
        // given
        Theme theme = Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        Theme savedTheme = themeRepository.save(theme);

        // when
        Optional<Theme> foundTheme = themeRepository.findById(savedTheme.id());

        // then
        Assertions.assertEquals(savedTheme.id(), foundTheme.get().id());
    }

    @Test
    @DisplayName("아이디를 기반으로 테마를 찾는다 - 없어도 오류가 발생하지 않는다. - Optional<Empty> 반환")
    void findById_success_even_if_no_theme() {
        // when
        Long notExistThemeId = 999L;
        Optional<Theme> foundTheme = themeRepository.findById(notExistThemeId);

        // then
        assertTrue(foundTheme.isEmpty());
    }

    @Test
    @DisplayName("모든 테마를 가져온다")
    void findAll_success() {
        // given
        Theme theme1 = Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        Theme theme2 = Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        themeRepository.save(theme1);
        themeRepository.save(theme2);

        // when
        List<Theme> result = themeRepository.findAll();

        // then
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @DisplayName("모든 테마를 가져온다 - 없는 경우에는 빈 리스트 반환")
    void findAll_success_even_if_no_theme() {
        // when
        List<Theme> result = themeRepository.findAll();

        // then
        Assertions.assertEquals(0, result.size());
    }

    @Test
    @DisplayName("아이디를 기반으로 테마를 삭제한다")
    void deleteById_success() {
        // given
        Theme theme = Theme.createWithNullId(TEST_THEMA_NAME, TEST_THEMA_DESCRIPTION, TEST_THEMA_THUMBNAIL);
        Theme savedTheme = themeRepository.save(theme);

        // when
        themeRepository.deleteById(savedTheme.id());

        // then
        Optional<Theme> result = themeRepository.findById(savedTheme.id());
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("없는 ID 기반으로 테마를 삭제해도 오류가 발생하지 않는다")
    void deleteById_success_even_if_no_theme() {
        // when
        Long notExistThemeId = 999L;
        themeRepository.deleteById(notExistThemeId);

        // then
        Assertions.assertDoesNotThrow(
                () -> themeRepository.deleteById(notExistThemeId)
        );
    }

    @Test
    @DisplayName("기간 내 예약 수가 많은 테마를 상위 N개 조회한다")
    void findTopNByPeriod_success() {
        // given
        Theme theme1 = themeRepository.save(
                Theme.createWithNullId("인기 테마", "설명1", "thumb1")
        );
        Theme theme2 = themeRepository.save(
                Theme.createWithNullId("덜 인기 테마", "설명2", "thumb2")
        );

        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 1L, "10:00");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 2L, "11:00");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 3L, "12:00");

        jdbcTemplate.update("""
            INSERT INTO reservation (name, date, time_id, theme_id)
            VALUES (?, ?, ?, ?)
            """, "예약자1", LocalDate.of(2026, 5, 1), 1L, theme1.id());

        jdbcTemplate.update("""
            INSERT INTO reservation (name, date, time_id, theme_id)
            VALUES (?, ?, ?, ?)
            """, "예약자2", LocalDate.of(2026, 5, 2), 2L, theme1.id());

        jdbcTemplate.update("""
            INSERT INTO reservation (name, date, time_id, theme_id)
            VALUES (?, ?, ?, ?)
            """, "예약자3", LocalDate.of(2026, 5, 3), 3L, theme2.id());

        // when
        List<Theme> result = themeRepository.findTopNByPeriod(
                LocalDate.of(2026, 5, 1),
                LocalDate.of(2026, 5, 7),
                ThemeSortType.POPULAR,
                2L
        );

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(theme1.id(), result.get(0).id());
        Assertions.assertEquals(theme2.id(), result.get(1).id());
    }
}