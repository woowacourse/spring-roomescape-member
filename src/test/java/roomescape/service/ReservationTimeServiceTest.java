package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.resetvationTime.ReservationTimeDao;
import roomescape.domain.ReservationTime;

class ReservationTimeServiceTest {

    List<ReservationTime> times = new ArrayList<>();
    ReservationTimeDao reservationTimeDao = new InMemoryReservationTimeDao(times);
    ReservationTimeService reservationTimeService = new ReservationTimeService(reservationTimeDao);

    @DisplayName("존재하지 않는 id로 예약 시간을 찾을 경우 예외가 발생한다.")
    @Test
    void findByIdThrowExceptionIfIdIsNotExist() {

        // given & when & then
        assertThatThrownBy(() -> reservationTimeService.findById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("예약 시간이 존재하지 않습니다.");
    }
}
