package roomescape.dao.reservationTime;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dao.reservation.JdbcReservationDao;
import roomescape.dao.theme.JdbcThemeDao;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import({JdbcReservationTimeDao.class, JdbcReservationDao.class, JdbcThemeDao.class})
@Sql({"/schema.sql", "/reservation-time-data.sql"})
class JdbcReservationTimeDaoTest {

    @Autowired
    private JdbcReservationTimeDao jdbcReservationTimeDao;

    @DisplayName("예약 시간을 데이터베이스에 추가한다.")
    @Test
    void addTest() {

        // given
        final ReservationTime reservationTime = new ReservationTime(LocalTime.of(10, 10));

        // when
        final ReservationTime savedReservationTime = jdbcReservationTimeDao.create(reservationTime);

        // then
        assertThat(savedReservationTime.getStartAt()).isEqualTo(LocalTime.of(10, 10));
    }

    @DisplayName("데이터이스에 있는 예약 시간 정보들을 가져온다.")
    @Test
    void findAllTest() {

        // when
        final List<ReservationTime> reservationTimes = jdbcReservationTimeDao.findAll();

        // then
        assertThat(reservationTimes.size()).isEqualTo(3);
    }

    @DisplayName("데이터베이스에서 예약 시간이 삭제될 경우 true를 반환한다.")
    @Test
    void deleteIfNoExistReservationReturnTrueTest() {

        // when
        final boolean result = jdbcReservationTimeDao.deleteIfNoReservation(2L);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("데이터베이스에서 예약이 존재하여 예약 시간이 삭제되지 않을 경우 false를 반환한다.")
    @Test
    void deleteIfExistReservationReturnFalseTest() {

        // when
        final boolean result = jdbcReservationTimeDao.deleteIfNoReservation(999L);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("데이터베이스에서 테마, 날짜로 예약된 시간을 찾는다.")
    @Test
    void findAllReservedByThemeAndDateTest() {

        // when
        final List<ReservationTime> optionalReservationTime = jdbcReservationTimeDao.findAllReservedByThemeAndDate(
                1L, LocalDate.of(2025, 4, 25));

        // then
        assertThat(optionalReservationTime.size()).isEqualTo(1);
    }

    @DisplayName("데이터베이스에 존재할 경우 true를 반환한다.")
    @Test
    void existsByIdReturnTrueTest() {

        // given
        final LocalTime time = LocalTime.of(10, 10);
        final ReservationTime savedReservationTime = jdbcReservationTimeDao.create(new ReservationTime(time));

        // when & then
        assertThat(jdbcReservationTimeDao.existsById(savedReservationTime.getId())).isTrue();
    }

    @DisplayName("데이터베이스에 존재하지 않을 경우 false를 반환한다.")
    @Test
    void nonExistsByIdReturnFalseTest() {

        // when & then
        assertThat(jdbcReservationTimeDao.existsById(5L)).isFalse();
    }
}
