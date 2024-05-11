package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.reservation.ReservationTime;
import java.util.List;
import java.util.Optional;

class ReservationTimeJdbcDaoTest extends DaoTest {

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Test
    @DisplayName("예약 시간을 저장한다.")
    void save() {
        // given
        final ReservationTime reservationTime = new ReservationTime("19:00");

        // when
        final ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);

        // when
        assertThat(savedReservationTime.getId()).isNotNull();
    }

    @Test
    @DisplayName("예약 시간 목록을 조회한다.")
    void findAll() {
        // given
        final ReservationTime reservationTime = new ReservationTime("19:00");
        reservationTimeDao.save(reservationTime);

        // when
        final List<ReservationTime> reservationTimes = reservationTimeDao.findAll();

        // then
        assertThat(reservationTimes).hasSize(1);
    }

    @Test
    @DisplayName("Id로 예약 시간을 조회한다.")
    void findById() {
        // given
        final ReservationTime savedReservationTime = reservationTimeDao.save(new ReservationTime("19:00"));

        // when
        final Optional<ReservationTime> reservationTime = reservationTimeDao.findById(savedReservationTime.getId());

        // then
        assertThat(reservationTime).isPresent();
    }

    @Test
    @DisplayName("존재하지 않는 Id로 예약 시간을 조회하면 빈 옵션을 반환한다.")
    void returnEmptyOptionalWhenFindByNotExistingId() {
        // given
        final Long notExistingId = 1L;

        // when
        final Optional<ReservationTime> reservationTime = reservationTimeDao.findById(notExistingId);

        // then
        assertThat(reservationTime).isEmpty();
    }

    @Test
    @DisplayName("Id로 예약 시간을 삭제한다.")
    void deleteById() {
        // given
        final ReservationTime savedReservationTime = reservationTimeDao.save(new ReservationTime("19:00"));

        // when
        reservationTimeDao.deleteById(savedReservationTime.getId());

        // then
        final List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        assertThat(reservationTimes).hasSize(0);
    }
}
