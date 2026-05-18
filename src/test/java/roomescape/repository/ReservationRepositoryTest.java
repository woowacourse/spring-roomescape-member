package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@JdbcTest
class ReservationRepositoryTest {
    private static final LocalDate DATE = LocalDate.of(2026, 5, 5);
    private static final LocalDateTime TEST_DATE_TIME = LocalDateTime.of(2000, 1, 1, 0, 0);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationRepository reservationRepository;


    @BeforeEach
    void setup() {
        jdbcTemplate.update("DELETE FROM reservation;");
        jdbcTemplate.update("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;");

        this.reservationRepository = new ReservationRepository(jdbcTemplate);
    }

    @Test
    void 예약_추가_테스트() {
        // given
        ReservationTime time = findTimeByStartAt("15:00");
        Theme theme = new Theme(1L, "테마 이름", "테마 설명", "썸네일");
        Reservation reservation = new Reservation("브라운", DATE, time, theme, TEST_DATE_TIME);

        // when & then
        Reservation savedReservation = reservationRepository.insert(reservation);
        List<Reservation> reservations = reservationRepository.findAll();
        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(savedReservation.getId()).isNotNull(),
                () -> assertThat(savedReservation.getName()).isEqualTo(reservation.getName()),
                () -> assertThat(savedReservation.getDate()).isEqualTo(reservation.getDate()),
                () -> assertThat(savedReservation.getTime().getStartAt()).isEqualTo(
                        reservation.getTime().getStartAt()));
    }

    @Test
    void 예약_삭제_테스트() {
        // given
        ReservationTime time1 = findTimeByStartAt("15:00");
        Theme theme1 = new Theme(1L, "테마 이름1", "테마 설명1", "썸네일1");
        ReservationTime time2 = findTimeByStartAt("12:00");
        Theme theme2 = new Theme(2L, "테마 이름2", "테마 설명2", "썸네일2");
        Reservation reservation1 = new Reservation("브라운", DATE, time1, theme1, TEST_DATE_TIME);
        Reservation reservation2 = new Reservation("구구", DATE, time2, theme2, TEST_DATE_TIME);
        Long id1 = reservationRepository.insert(reservation1).getId();
        Long id2 = reservationRepository.insert(reservation2).getId();

        // when
        int deletedCount = reservationRepository.delete(id1);

        // then
        List<Reservation> reservations = reservationRepository.findAll();
        assertAll(
                () -> assertThat(deletedCount).isEqualTo(1),
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservationRepository.existsById(id1)).isFalse());
    }

    private ReservationTime findTimeByStartAt(String startAt) {
        String sql = "SELECT id, start_at FROM reservation_time WHERE start_at = ?;";
        return jdbcTemplate.queryForObject(
                sql,
                (resultSet, rowNum) -> {
                    return new ReservationTime(
                            resultSet.getLong("id"),
                            resultSet.getObject("start_at", LocalTime.class));
                }, startAt);
    }
}
