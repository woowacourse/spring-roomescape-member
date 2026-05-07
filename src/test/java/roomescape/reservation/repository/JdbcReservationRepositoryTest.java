package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Time;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@JdbcTest
class JdbcReservationRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    ReservationRepository reservationRepository;

    @Autowired
    public JdbcReservationRepositoryTest(JdbcTemplate jdbcTemplate) {
        this.reservationRepository = new JdbcReservationRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("예약을 저장하고 반환된 객체의 ID를 확인한다.")
    void saveTest() {
        // given
        ReservationTime time = createTime(LocalTime.of(10, 0));
        Theme theme = createTheme("우테코", "우테코 전용 테마", "https://example.com");

        Reservation reservation = new Reservation(
                null,
                "브라운",
                LocalDate.of(2024, 5, 1),
                time,
                theme
        );

        // when
        Reservation saved = reservationRepository.save(reservation);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("브라운");
    }

    @Test
    @DisplayName("ID를 통해 예약을 삭제한다.")
    void deleteByIdTest() {
        // given
        ReservationTime time = createTime(LocalTime.of(10, 0));
        Theme theme = createTheme("우테코", "우테코 전용 테마", "https://example.com");

        Reservation saved = saveReservation("브라운",  LocalDate.of(2024, 5, 1), time, theme);

        // when
        reservationRepository.deleteById(saved.getId());

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertThat(reservations).isEmpty();
    }

    @Test
    @DisplayName("모든 예약 목록을 조회한다. (Join 확인)")
    void findAllTest() {
        // given
        ReservationTime time = createTime(LocalTime.of(10, 0));
        Theme theme = createTheme("우테코", "우테코 전용 테마", "https://example.com");

        Reservation saved1 = saveReservation("브라운", LocalDate.of(2024, 5, 1), time, theme);
        Reservation saved2 = saveReservation("브라운", LocalDate.of(2024, 5, 2), time, theme);
        Reservation saved3 = saveReservation("브라운", LocalDate.of(2024, 5, 3), time, theme);

        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(3);
        assertThat(reservations).containsExactly(saved1, saved2, saved3);
    }

    @Test
    @DisplayName("특정 날짜와 시간 ID로 예약 존재 여부를 확인한다.")
    void existsByDateAndTimeIdAndThemeIdTest() {
        // given
        ReservationTime time = createTime(LocalTime.of(10, 0));
        LocalDate date = LocalDate.of(2024, 5, 1);
        Theme theme = createTheme("우테코", "우테코 전용 테마", "https://example.com");

        saveReservation("브라운", date, time, theme);

        // when
        boolean exists = reservationRepository.existsByDateAndTimeIdAndThemeId(date, time.getId(), theme.getId());
        boolean notExists = reservationRepository.existsByDateAndTimeIdAndThemeId(date, 999L, theme.getId());

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("from과 to 사이 일정의 예약들에 대해, 상위 limit 개의 테마들을 조회한다.")
    void findPopularThemesTest() {
        // given
        Theme woowaTheme = createTheme("우테코", "우테코 전용 테마", "https://example.com");
        Theme pairTheme = createTheme("페어", "페어 전용 테마", "https://pair.com");
        Theme carrotTheme = createTheme("당근", "당근 전용 테마", "https://carrot.com");

        ReservationTime time = createTime(LocalTime.of(10, 0));

        LocalDate today = LocalDate.now(
                Clock.fixed(
                        Instant.parse("2026-05-06T00:00:00Z"),
                        ZoneId.of("Asia/Seoul")
                )
        );

        saveReservation("브라운",today.minusDays(1), time, woowaTheme);
        saveReservation("포비",today.minusDays(2), time, woowaTheme);
        saveReservation("제이슨",today.minusDays(3), time, woowaTheme);
        saveReservation("이든", today.minusDays(1), time, pairTheme);
        saveReservation("레아", today.minusDays(2), time, pairTheme);
        saveReservation("웨지", today.minusDays(1), time, carrotTheme);
        saveReservation("오늘예약", today, time, carrotTheme);
        saveReservation("범위밖예약", today.minusDays(8), time, carrotTheme);

        // when
        List<PopularThemeQueryResult> popularThemes = reservationRepository.findPopularThemes(
                LocalDate.of(2026, 4, 29),
                LocalDate.of(2026, 5, 5),
                2
        );

        // then
        assertThat(popularThemes)
                .extracting(PopularThemeQueryResult::name)
                .containsExactly("우테코", "페어");
    }

    private ReservationTime createTime(LocalTime time) {
        jdbcTemplate.update(
                "INSERT INTO reservation_time (start_at) VALUES (?)",
                Time.valueOf(time)
        );

        long timeId = jdbcTemplate.queryForObject(
                "SELECT id FROM reservation_time WHERE start_at = ?",
                Long.class,
                Time.valueOf(time)
        );

        return new ReservationTime(timeId, time);
    }


    private Theme createTheme(String name, String description, String thumbnailUrl) {
        jdbcTemplate.update(
                "INSERT INTO theme (name, description, thumbnail_url) VALUES (?, ?, ?)",
                name, description, thumbnailUrl
        );

        Long themeId = jdbcTemplate.queryForObject(
                "SELECT id FROM theme WHERE name = ?",
                Long.class,
                name
        );

        return new Theme(themeId, name, description, thumbnailUrl);
    }

    private Reservation saveReservation(String name, LocalDate date, ReservationTime time, Theme theme) {
        return reservationRepository.save(
                new Reservation(null, name, date, time, theme)
        );
    }
}
