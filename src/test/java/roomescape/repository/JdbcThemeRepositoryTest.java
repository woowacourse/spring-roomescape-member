package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

@JdbcTest
@Sql(scripts = "/test-setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
        Optional<Theme> foundTheme = jdbcThemeRepository.findById(savedTheme.id());
        assertThat(foundTheme).isPresent();
        assertThat(foundTheme.get().name()).isEqualTo("공포");
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
        insertTimeSlot();
        insertReservation(savedTheme.id());
        List<Theme> themes = jdbcThemeRepository.findPopularThemes(10L, LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1));
        assertThat(themes).hasSize(1);
    }

    @Test
    @DisplayName("존재하는 테마를 삭제한다.")
    void deleteExisting() {
        Theme savedTheme = jdbcThemeRepository.save(Theme.transientOf("공포", "귀신의 집", "https://url"));
        jdbcThemeRepository.deleteById(savedTheme.id());
        assertThat(jdbcThemeRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제해도 예외가 발생하지 않는다.")
    void deleteNonExisting() {
        assertThatCode(() -> jdbcThemeRepository.deleteById(999L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("존재하는 테마의 정보를 수정한다.")
    void updateExisting() {
        Theme savedTheme = jdbcThemeRepository.save(Theme.transientOf("공포", "귀신의 집", "https://url"));
        Theme updateTheme = new Theme(savedTheme.id(), "코믹", "웃긴 집", "https://url2");
        assertThat(jdbcThemeRepository.update(updateTheme)).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하지 않는 테마를 수정하면 0을 반환한다.")
    void updateNonExisting() {
        Theme updateTheme = new Theme(999L, "코믹", "웃긴 집", "https://url2");
        assertThat(jdbcThemeRepository.update(updateTheme)).isZero();
    }

    private void insertTimeSlot() {
        String sql = "INSERT INTO time_slot (start_at) VALUES ('10:00:00')";
        jdbcTemplate.execute(sql);
    }

    private void insertReservation(long themeId) {
        String sql = "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, "test", LocalDate.now(), 1L, themeId);
    }
}
