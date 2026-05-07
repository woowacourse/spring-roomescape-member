package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;

@JdbcTest
@Sql("/test-setup.sql")
class JdbcThemeRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcThemeRepository jdbcThemeRepository;

    @BeforeEach
    void setUp() {
        jdbcThemeRepository = new JdbcThemeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("테마를 저장하고 영속화된 객체를 반환한다.")
    void save() {
        Theme theme = Theme.transientOf("공포", "귀신의 집", "https://url");
        Theme savedTheme = jdbcThemeRepository.save(theme);
        assertThat(savedTheme.id()).isPositive();
    }

    @Test
    @DisplayName("식별자로 테마를 조회한다.")
    void findById() {
        Theme savedTheme = jdbcThemeRepository.save(Theme.transientOf("공포", "귀신의 집", "https://url"));
        Theme foundTheme = jdbcThemeRepository.findById(savedTheme.id());
        assertThat(foundTheme.name()).isEqualTo("공포");
    }

    @Test
    @DisplayName("모든 테마 목록을 조회한다.")
    void findAll() {
        jdbcThemeRepository.save(Theme.transientOf("공포", "귀신의 집", "https://url"));
        List<Theme> themes = jdbcThemeRepository.findAll();
        assertThat(themes).hasSize(1);
    }

    @Test
    @DisplayName("기간 내 인기 테마를 예약 건수 기반으로 조회한다.")
    void findPopularThemes() {
        Theme savedTheme = jdbcThemeRepository.save(Theme.transientOf("공포", "귀신의 집", "https://url"));
        insertReservation(savedTheme.id());
        List<Theme> themes = jdbcThemeRepository.findPopularThemes(10L, LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1));
        assertThat(themes).hasSize(1);
    }

    private void insertReservation(long themeId) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, "test", LocalDate.now(), 1L, themeId);
    }
}
