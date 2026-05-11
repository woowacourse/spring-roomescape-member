package roomescape.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.Reservation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationDao reservationDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at, status) VALUES (1, '10:00', 'AVAILABLE')");
        jdbcTemplate.update("INSERT INTO theme (id, name, thumbnail_url, description, status) VALUES (1, '공포의 저택', 'url', '설명', 'AVAILABLE')");
    }

    @Test
    @DisplayName("예약 저장 테스트")
    void saveReservation() {
        Reservation reservation = Reservation.pending("user_a", LocalDate.of(2026, 5, 1));
        Reservation saved = reservationDao.save(reservation, 1L, 1L);

        assertThat(saved.username()).isEqualTo("user_a");
        assertThat(saved.reservationDate()).isEqualTo(LocalDate.of(2026, 5, 1));
        assertThat(saved.reservationTime().id()).isEqualTo(1L);
        assertThat(saved.reservationTheme().id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("예약 삭제 시 상태가 DELETED로 변경된다")
    void deleteReservation() {
        Reservation reservation = Reservation.pending("user_a", LocalDate.of(2026, 5, 1));
        Reservation saved = reservationDao.save(reservation, 1L, 1L);

        reservationDao.delete(saved.id());

        List<Reservation> reservations = reservationDao.findAllReservations();
        assertThat(reservations.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("DELETED 상태 예약은 전체 조회에서 제외된다")
    void findAllExcludesDeleted() {
        Reservation reservation = Reservation.pending("user_a", LocalDate.of(2026, 5, 1));
        Reservation saved = reservationDao.save(reservation, 1L, 1L);
        reservationDao.delete(saved.id());

        List<Reservation> reservations = reservationDao.findAllReservations();
        assertThat(reservations.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("동일 날짜·시간·테마 예약이 존재하면 true를 반환한다")
    void existsReturnsTrueWhenDuplicate() {
        Reservation reservation = Reservation.pending("user_a", LocalDate.of(2026, 5, 1));
        reservationDao.save(reservation, 1L, 1L);

        boolean exists = reservationDao.existsByDateAndTimeIdAndThemeId(LocalDate.of(2026, 5, 1), 1L, 1L);
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("동일 날짜·시간·테마 예약이 없으면 false를 반환한다")
    void existsReturnsFalseWhenNoDuplicate() {
        boolean exists = reservationDao.existsByDateAndTimeIdAndThemeId(LocalDate.of(2026, 5, 1), 1L, 1L);
        assertThat(exists).isFalse();
    }
}
