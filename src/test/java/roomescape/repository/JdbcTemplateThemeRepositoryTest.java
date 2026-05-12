package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("/test-theme.sql")
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
class JdbcTemplateThemeRepositoryTest {
    private static final int DEFAULT_THEME_SIZE = 10;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        this.themeRepository = new JdbcTemplateThemeRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("모든 테마를 조회한다")
    void findAllThemes() {
        List<Theme> themes = themeRepository.findAll();

        assertThat(themes.size()).isEqualTo(DEFAULT_THEME_SIZE);
    }

    @Test
    @DisplayName("테마를 저장한다")
    void saveTheme() {
        Theme theme = new Theme(null, "마법 학교", "마법 학교의 마지막 시험을 통과하세요.", "https://example.com/theme10.jpg");

        Theme savedTheme = themeRepository.save(theme);

        assertThat(savedTheme.id()).isNotNull();
        assertThat(savedTheme.name()).isEqualTo("마법 학교");
        assertThat(savedTheme.description()).isEqualTo("마법 학교의 마지막 시험을 통과하세요.");
        assertThat(savedTheme.thumbnailUrl()).isEqualTo("https://example.com/theme10.jpg");
    }

    @Test
    @DisplayName("테마를 삭제한다")
    void deleteThemeById() {
        Theme theme = new Theme(null, "마법 학교", "마법 학교의 마지막 시험을 통과하세요.", "https://example.com/theme10.jpg");
        long id = themeRepository.save(theme).id();

        themeRepository.delete(id);
        assertThat(themeRepository.findAll().size()).isEqualTo(DEFAULT_THEME_SIZE);
    }

    @Test
    @DisplayName("예약되지 않은 시간을 조회한다")
    @Sql("/test-reservation-time.sql")
    @Sql("/test-reservation.sql")
    void findAvailableTimes_WhenReservedTimesExist() {
        LocalDate date = LocalDate.of(2021, 1, 1);

        List<ReservationTime> reservationTimes = themeRepository.findAvailableTimes(1L, date);

        assertThat(reservationTimes.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 건수가 많은 순서로 인기 테마를 반환한다")
    @Sql("/test-reservation-time.sql")
    void findPopularThemesInOrder_WhenReservationsExist() {
        LocalDate inRange = LocalDate.of(2026, 5, 1);
        addReservation("A", inRange, 1, 1);
        addReservation("B", inRange, 2, 1);
        addReservation("C", inRange, 3, 1);
        addReservation("D", inRange, 1, 2);
        addReservation("E", inRange, 2, 2);
        addReservation("F", inRange, 1, 3);

        List<Theme> popular = themeRepository.findPopularThemes(inRange, inRange, 3);

        assertThat(popular).hasSize(3);
        assertThat(popular.get(0).id()).isEqualTo(1L);
        assertThat(popular.get(1).id()).isEqualTo(2L);
        assertThat(popular.get(2).id()).isEqualTo(3L);
    }

    private void addReservation(String name, LocalDate date, long timeId, long themeId) {
        jdbcTemplate.update(
                "INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                name, date, timeId, themeId);
    }

    @Test
    @DisplayName("인기 테마를 limit 개수만큼 반환한다")
    @Sql("/test-reservation-time.sql")
    void findPopularThemesWithLimit() {
        LocalDate inRange = LocalDate.of(2026, 5, 1);
        addReservation("A", inRange, 1, 1);
        addReservation("B", inRange, 1, 2);
        addReservation("C", inRange, 1, 3);

        List<Theme> popular = themeRepository.findPopularThemes(inRange, inRange, 2);

        assertThat(popular).hasSize(2);
    }

    @Test
    @DisplayName("기간 밖 예약은 인기 테마 집계에서 제외한다")
    @Sql("/test-reservation-time.sql")
    void excludeReservations_WhenOutOfRange() {
        LocalDate inRange = LocalDate.of(2026, 5, 1);
        LocalDate outOfRange = LocalDate.of(2026, 4, 1);

        addReservation("A", outOfRange, 1, 1);
        addReservation("B", outOfRange, 2, 1);
        addReservation("C", outOfRange, 3, 1);
        addReservation("D", outOfRange, 1, 1);
        addReservation("E", outOfRange, 2, 1);
        addReservation("F", inRange, 1, 1);

        addReservation("G", inRange, 2, 2);
        addReservation("H", inRange, 3, 2);

        List<Theme> popular = themeRepository.findPopularThemes(inRange, inRange, 2);

        assertThat(popular).hasSize(2);
        assertThat(popular.get(0).id()).isEqualTo(2L);
        assertThat(popular.get(1).id()).isEqualTo(1L);
    }
}
