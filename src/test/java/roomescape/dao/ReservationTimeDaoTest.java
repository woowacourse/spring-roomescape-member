package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.ReservationTime;

@JdbcTest
@Import(ReservationTimeDao.class)
class ReservationTimeDaoTest {

    @Autowired
    private ReservationTimeDao timeDao;

    @Test
    void 예약_시간을_생성한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTime reservationTime = ReservationTime.createWithoutId(startAt);

        // when
        ReservationTime savedReservationTime = timeDao.insert(reservationTime);

        // then
        assertAll(
                () -> assertThat(savedReservationTime.getId()).isNotNull(),
                () -> assertThat(savedReservationTime.getStartAt()).isEqualTo(startAt)
        );
    }

    @Test
    void 예약_시간_목록을_조회한다() {
        // given
        timeDao.insert(ReservationTime.createWithoutId(LocalTime.of(10, 0)));
        timeDao.insert(ReservationTime.createWithoutId(LocalTime.of(11, 0)));
        timeDao.insert(ReservationTime.createWithoutId(LocalTime.of(12, 0)));
        timeDao.insert(ReservationTime.createWithoutId(LocalTime.of(13, 0)));
        timeDao.insert(ReservationTime.createWithoutId(LocalTime.of(14, 0)));

        // when
        List<ReservationTime> reservationTimes = timeDao.selectAll();

        // then
        assertAll(
                () -> assertThat(reservationTimes).hasSize(5),
                () -> assertThat(reservationTimes.get(0).getStartAt()).isEqualTo(LocalTime.of(10, 0))
        );
    }

    @Test
    void 아이디에_맞는_예약_시간을_조회한다() {
        // given
        ReservationTime reservationTime = timeDao.insert(ReservationTime.createWithoutId(LocalTime.of(10, 0)));

        // when
        Optional<ReservationTime> selectReservationTime = timeDao.selectById(reservationTime.getId());

        // then
        assertAll(
                () -> assertThat(selectReservationTime).isPresent(),
                () -> assertThat(selectReservationTime.get().getStartAt()).isEqualTo(reservationTime.getStartAt())
        );
    }

    @Test
    void 예약_시간을_삭제한다() {
        // given
        ReservationTime savedReservationTime = timeDao.insert(ReservationTime.createWithoutId(LocalTime.of(10, 0)));

        // when
        timeDao.delete(savedReservationTime.getId());

        // then
        List<ReservationTime> reservationTimes = timeDao.selectAll();
        assertThat(reservationTimes).hasSize(0);
    }
}
