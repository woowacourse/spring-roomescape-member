package roomescape.domain.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.time.entity.ReservationTime;
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
        reservationRepository = new ReservationJdbcRepository(jdbcTemplate, dataSource);

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
                .containsExactlyInAnyOrder("흑곰", "카키", "피온", "워넬", "포비", "네오", "브리",
                        "워니", "브라운", "제임스", "로치", "이안", "스타크");
    }

    @Test
    @DisplayName("사용자 이름으로 예약 목록을 조회한다.")
    void findByUsernameTest() {
        // when
        List<Reservation> reservations = reservationRepository.findByUsername("흑곰");

        // then
        assertThat(reservations).hasSize(1);
        assertThat(reservations.getFirst().getUsername()).isEqualTo("흑곰");
        assertThat(reservations.getFirst().getDate()).isEqualTo(LocalDate.of(2026, 5, 5));
        assertThat(reservations.getFirst().getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(reservations.getFirst().getTheme().getName()).isEqualTo("워너비");
    }

    @Test
    @DisplayName("ID로 예약을 조회한다.")
    void findByIdTest() {
        // when
        Reservation reservation = reservationRepository.findById(1L).get();

        // then
        assertThat(reservation.getId()).isEqualTo(1L);
        assertThat(reservation.getUsername()).isEqualTo("흑곰");
        assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2026, 5, 5));
        assertThat(reservation.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
        assertThat(reservation.getTheme().getName()).isEqualTo("워너비");
    }

    @Test
    @DisplayName("예약을 저장한다.")
    void saveTest() {
        // given
        Theme theme = new Theme(3L, "우주 정거장", "우주에서 살아남으세요.", "https://example.com/space.png");
        ReservationTime time = new ReservationTime(6L, LocalTime.of(15, 0));
        Reservation reservation = new Reservation("조이", theme, LocalDate.of(2026, 5, 10), time);

        // when
        Reservation saved = reservationRepository.save(reservation);

        // then
        assertThat(saved.getId()).isNotNull();

        Reservation found = findReservationById(saved.getId());
        assertThat(found.getUsername()).isEqualTo("조이");
        assertThat(found.getTheme().getId()).isEqualTo(3L);
        assertThat(found.getTime().getId()).isEqualTo(6L);
    }

    @Test
    @DisplayName("예약 날짜와 시간을 수정한다.")
    void updateTest() {
        // given
        Reservation reservation = reservationRepository.findById(1L).get();
        Reservation updatedReservation = new Reservation(
                reservation.getId(),
                reservation.getUsername(),
                reservation.getTheme(),
                LocalDate.of(9999, 5, 8),
                new ReservationTime(6L, LocalTime.of(15, 0))
        );

        // when
        reservationRepository.update(updatedReservation);

        // then
        Reservation found = findReservationById(1L);
        assertThat(found.getDate()).isEqualTo(LocalDate.of(9999, 5, 8));
        assertThat(found.getTime().getId()).isEqualTo(6L);
    }

    @Test
    @DisplayName("ID로 예약을 삭제한다.")
    void deleteByIdTest() {
        // given
        long generatedId = insertReservation("삭제될유저", 4L, LocalDate.of(2026, 5, 11), 1L);

        // when
        reservationRepository.deleteById(generatedId);

        // then
        List<Reservation> reservations = findAllReservationById(generatedId);
        assertThat(reservations).isEmpty();
    }

    private long insertReservation(String username, long themeId, LocalDate date, long timeId) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("theme_id", themeId)
                .addValue("date", date)
                .addValue("time_id", timeId);

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    private Reservation findReservationById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.queryForObject(
                "SELECT * FROM reservation WHERE id = :id",
                parameters,
                reservationRowMapper()
        );
    }

    private List<Reservation> findAllReservationById(Long id) {
        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id);

        return jdbcTemplate.query(
                "SELECT * FROM reservation WHERE id = :id",
                parameters,
                reservationRowMapper()
        );
    }

    private RowMapper<Reservation> reservationRowMapper() {
        return (resultSet, rowNum) -> new Reservation(
                resultSet.getLong("id"),
                resultSet.getString("username"),
                new Theme(resultSet.getLong("theme_id"), null, null, null),
                LocalDate.parse(resultSet.getString("date")),
                new ReservationTime(resultSet.getLong("time_id"), null)
        );
    }
}
