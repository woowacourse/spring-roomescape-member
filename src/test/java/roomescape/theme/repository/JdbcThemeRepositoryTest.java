package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import roomescape.theme.domain.Theme;
import roomescape.theme.service.dto.ThemeBestServiceDto;

@JdbcTest
@Import(JdbcThemeRepository.class)
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcThemeRepository jdbcThemeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void findAll() {
        assertThat(jdbcThemeRepository.findAll()).isEmpty();

        Theme saved = jdbcThemeRepository.save(new Theme("이름", "설명", "https://img.test/a.png"));

        List<Theme> themes = jdbcThemeRepository.findAll();
        assertThat(themes).hasSize(1);
        assertThat(themes.get(0).getId()).isEqualTo(saved.getId());
        assertThat(themes.get(0).getName()).isEqualTo("이름");
        assertThat(themes.get(0).getDescription()).isEqualTo("설명");
        assertThat(themes.get(0).getImageUrl()).isEqualTo("https://img.test/a.png");
    }

    @Test
    void save() {
        Theme saved = jdbcThemeRepository.save(new Theme("테마", "내용", "https://img.test/b.png"));

        assertThat(saved.getId()).isPositive();
        assertThat(saved.getName()).isEqualTo("테마");
        assertThat(saved.getDescription()).isEqualTo("내용");
        assertThat(saved.getImageUrl()).isEqualTo("https://img.test/b.png");
    }

    @Test
    void findById() {
        Theme saved = jdbcThemeRepository.save(new Theme("테마", "설명", "https://img.test/a.png"));

        assertThat(jdbcThemeRepository.findById(saved.getId()))
                .hasValueSatisfying(theme -> {
                    assertThat(theme.getId()).isEqualTo(saved.getId());
                    assertThat(theme.getName()).isEqualTo("테마");
                    assertThat(theme.getDescription()).isEqualTo("설명");
                    assertThat(theme.getImageUrl()).isEqualTo("https://img.test/a.png");
                });
    }

    @Test
    void findById_없으면_빈_Optional을_반환한다() {
        assertThat(jdbcThemeRepository.findById(1L))
                .isEmpty();
    }

    @Test
    void deleteById() {
        Theme saved = jdbcThemeRepository.save(new Theme("x", "y", "https://img.test/c.png"));

        assertThat(jdbcThemeRepository.deleteById(saved.getId())).isTrue();
        assertThat(jdbcThemeRepository.findAll()).isEmpty();

        assertThat(jdbcThemeRepository.deleteById(saved.getId())).isFalse();
    }

    @Test
    void existsById() {
        assertThat(jdbcThemeRepository.existsById(1L)).isFalse();

        Theme saved = jdbcThemeRepository.save(new Theme("테마", "설명", "https://img.test/a.png"));

        assertThat(jdbcThemeRepository.existsById(saved.getId())).isTrue();
        assertThat(jdbcThemeRepository.existsById(saved.getId() + 1)).isFalse();
    }

    @Test
    void findBestThemesByDate() {
        Long timeId = insertTime("10:00", "12:00");

        Theme theme1 = jdbcThemeRepository.save(new Theme("테마1", "설명1", "https://img.test/1.png"));
        Theme theme2 = jdbcThemeRepository.save(new Theme("테마2", "설명2", "https://img.test/2.png"));
        Theme theme3 = jdbcThemeRepository.save(new Theme("테마3", "설명3", "https://img.test/3.png"));

        LocalDate date = LocalDate.of(2026, 5, 10);
        int dayCount = 7;

        insertReservation("a1", LocalDate.of(2026, 5, 7), timeId, theme1.getId());
        insertReservation("a2", LocalDate.of(2026, 5, 8), timeId, theme1.getId());
        insertReservation("a3", LocalDate.of(2026, 5, 10), timeId, theme1.getId());

        insertReservation("b1", LocalDate.of(2026, 5, 9), timeId, theme2.getId());
        insertReservation("b2", LocalDate.of(2026, 5, 10), timeId, theme2.getId());
        insertReservation("b_in", LocalDate.of(2026, 5, 6), timeId, theme2.getId());

        insertReservation("c_out", LocalDate.of(2026, 5, 2), timeId, theme3.getId());
        insertReservation("c_today", LocalDate.of(2026, 5, 10), timeId, theme3.getId());

        // when
        List<Theme> bestThemes = jdbcThemeRepository.findBestThemesByDate(new ThemeBestServiceDto(date, dayCount, 10));

        // then
        assertThat(bestThemes).hasSize(2);
        assertThat(bestThemes.get(0).getId()).isEqualTo(theme1.getId());
        assertThat(bestThemes.get(1).getId()).isEqualTo(theme2.getId());
        assertThat(bestThemes).extracting(Theme::getName)
                .containsExactly("테마1", "테마2");
    }

    private Long insertTime(String startAt, String endAt) {
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_time, end_time) VALUES (?, ?)",
                startAt,
                endAt
        );
        return jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_time = ?",
                Long.class,
                startAt
        );
    }

    private void insertReservation(String name, LocalDate date, Long timeId, Long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                name,
                java.sql.Date.valueOf(date),
                timeId,
                themeId
        );
    }
}
