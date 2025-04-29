package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.reservation.ReservationDao;
import roomescape.dao.resetvationTime.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.ReservationExistException;

class ReservationTimeServiceTest {

    ReservationTimeDao reservationTimeDao = new InMemoryReservationTimeDao(new ArrayList<>());
    ReservationDao reservationDao = new InMemoryReservationDao(new ArrayList<>(), reservationTimeDao);
    ReservationService reservationService = new ReservationService(reservationDao, reservationTimeDao);
    ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeDao, reservationService);

    @DisplayName("예약 시간 삭제 시, 해당 시간의 예약이 존재하면 예외가 발생한다.")
    @Test
    void deleteReservationTimeIfExistedReservationThrowExceptionTest() {

        // given
        ReservationTime reservationTime = reservationTimeDao.create(new ReservationTime(LocalTime.of(12, 1)));
        Reservation reservation = reservationDao.create(
                new Reservation("test", LocalDate.of(2025, 12, 1), reservationTime));

        // when & then
        Assertions.assertThatThrownBy(() -> reservationTimeService.delete(reservationTime.getId()))
                .isInstanceOf(ReservationExistException.class)
                .hasMessage("이 시간에 대한 예약이 존재합니다.");
    }
}
