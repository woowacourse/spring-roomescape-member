package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import roomescape.BasicAcceptanceTest;
import roomescape.dto.request.ReservationTimeRequest;

@Sql("/setReservationTime.sql")
class ReservationTimeServiceTest extends BasicAcceptanceTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @DisplayName("중복된 예약 시간을 저장할 시 예외를 발생시킨다")
    @Test
    void rejectDuplicateReservationTime() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest("10:05");

        assertThatThrownBy(() -> reservationTimeService.save(reservationTimeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 예약 시간이 존재합니다. 입력한 시간: " + reservationTimeRequest.startAt());
    }
}
