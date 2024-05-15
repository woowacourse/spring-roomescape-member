package roomescape.reservation.dao;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.domain.Theme;

@JdbcTest
@Sql(scripts = "/data-test.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationDaoTest {

    private static final LocalDate TODAY = LocalDate.now();

    private final ReservationJdbcDao reservationDao;

    @Autowired
    public ReservationDaoTest(DataSource dataSource) {
        this.reservationDao = new ReservationJdbcDao(dataSource);
    }

    @Test
    @DisplayName("데이터를 정상적으로 저장한다.")
    void saveReservation() {
        Reservation reservation = Reservation.saveReservationOf(TODAY, 1L, 1L, 1L);
        reservationDao.save(reservation);

        Assertions.assertThat(reservation.getId()).isEqualTo(3);
    }

    @Test
    @DisplayName("데이터를 정상적으로 조회한다.")
    void getReservations() {
        List<Reservation> reservations = reservationDao.findAllReservationOrderByDateAndTimeStartAt();

        Assertions.assertThat(reservations.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("지난 7일 기준 예약이 많은 테마 순으로 조회한다.")
    void getTopReservationThemes() {
        List<Theme> themes = reservationDao.findThemeByDateOrderByThemeIdCountLimit(LocalDate.of(2024, 4, 24),
                LocalDate.of(2024, 5, 1), 10);

        Assertions.assertThat(themes.get(0).getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("데이터를 정상적으로 삭제한다.")
    void deleteReservations() {
        reservationDao.deleteById(1L);

        Assertions.assertThat(reservationDao.findAllReservationOrderByDateAndTimeStartAt().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("존재하는 예약시간인지 확인한다.")
    void countReservationTime() {
        Assertions.assertThat(reservationDao.existsByTimeId(1L)).isTrue();
    }
}
