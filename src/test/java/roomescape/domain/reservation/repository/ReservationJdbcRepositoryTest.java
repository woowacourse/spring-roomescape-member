package roomescape.domain.reservation.repository;

import static java.sql.Date.valueOf;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservationtime.entity.ReservationTime;
import roomescape.domain.theme.entity.Theme;

@JdbcTest
class ReservationJdbcRepositoryTest {

    @Autowired
    DataSource dataSource;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    SimpleJdbcInsert simpleJdbcInsert;

    ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new ReservationJdbcRepository(jdbcTemplate);

        simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    @DisplayName("모든 예약을 조회한다.")
    void findAllTest() {
        // when
        List<Reservation> reservations = reservationRepository.findAll();

        // then
        assertThat(reservations).hasSize(13);
        assertThat(reservations)
                .extracting(Reservation::getUsername)
                .containsExactlyInAnyOrder(
                        "흑곰", "카키", "피온", "워넬", "포비", "네오", "브리",
                        "워니", "브라운", "제임스", "로치", "이안", "스타크"
                );
    }

    @Test
    @DisplayName("이름으로 예약을 조회한다.")
    void findAllByUsernameTest() {
        // when
        List<Reservation> reservations = reservationRepository.findAllByUsername("포비");

        // then
        assertThat(reservations).hasSize(1);
        assertThat(reservations)
                .extracting(Reservation::getUsername)
                .containsExactly("포비");
    }

    @Test
    @DisplayName("ID로 예약을 조회한다.")
    void findByIdTest() {
        // given
        long generatedId = insertReservation(
                "조이",
                3L,
                LocalDate.of(2026, 5, 10),
                6L
        );

        // when
        Reservation found = reservationRepository.findById(generatedId).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getUsername()).isEqualTo("조이");
    }

    @Test
    @DisplayName("예약을 저장한다.")
    void saveTest() {
        // given
        Theme theme = Theme.of(
                3L,
                "우주 정거장",
                "우주에서 살아남으세요.",
                "https://example.com/space.png"
        );
        ReservationTime time = ReservationTime.of(6L, LocalTime.of(15, 0));
        Reservation reservation = Reservation.create(
                "조이",
                theme,
                LocalDate.of(2026, 5, 10),
                time
        );

        // when
        Reservation saved = reservationRepository.save(reservation);

        // then
        assertThat(saved.getId()).isNotNull();

        Map<String, Object> found = findReservationById(saved.getId());
        assertThat(found.get("username")).isEqualTo("조이");
        assertThat(((Number) found.get("theme_id")).longValue()).isEqualTo(3L);
        assertThat(((Number) found.get("time_id")).longValue()).isEqualTo(6L);
    }

    @Test
    @DisplayName("예약을 수정한다.")
    void updateTest() {
        // given
        long generatedId = insertReservation(
                "조이",
                3L,
                LocalDate.of(2026, 5, 10),
                6L
        );
        Reservation reservation = Reservation.of(
                generatedId,
                "조이",
                Theme.of(3L, "theme", "desc", "url"),
                LocalDate.of(2026, 5, 11),
                ReservationTime.of(1L, LocalTime.of(10, 0))
        );

        // when
        reservationRepository.update(generatedId, reservation);

        // then
        Map<String, Object> found = findReservationById(generatedId);
        assertThat(found.get("date")).isEqualTo(valueOf(LocalDate.of(2026, 5, 11)));
        assertThat(((Long) found.get("time_id"))).isEqualTo(1L);
    }

    @Test
    @DisplayName("ID로 예약을 삭제한다.")
    void deleteByIdTest() {
        // given
        long generatedId = insertReservation(
                "삭제될유저",
                4L,
                LocalDate.of(2026, 5, 11),
                1L
        );

        // when
        reservationRepository.deleteById(generatedId);

        // then
        List<Map<String, Object>> reservations = findAllReservationById(generatedId);
        assertThat(reservations).isEmpty();
    }

    @Test
    @DisplayName("테마 ID, 날짜, 시간 ID로 예약 존재 여부를 확인한다.")
    void existsByThemeIdAndDateAndTimeId() {
        // given
        long themeId = 1L;
        LocalDate date = LocalDate.of(2026, 5, 10);
        long timeId = 1L;
        insertReservation("유저", themeId, date, timeId);

        // when
        boolean exists = reservationRepository.existsByThemeIdAndDateAndTimeId(themeId, date, timeId);
        boolean notExists = reservationRepository.existsByThemeIdAndDateAndTimeId(themeId, date, 2L);

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    private long insertReservation(String username, long themeId, LocalDate date, long timeId) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("theme_id", themeId)
                .addValue("date", date)
                .addValue("time_id", timeId);

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    private Map<String, Object> findReservationById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.queryForMap(
                "SELECT * FROM reservation WHERE id = :id",
                parameters
        );
    }

    private List<Map<String, Object>> findAllReservationById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.queryForList(
                "SELECT * FROM reservation WHERE id = :id",
                parameters
        );
    }
}
