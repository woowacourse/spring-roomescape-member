package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.service.request.ReservationTimeRequest;
import roomescape.support.IntegrationTestSupport;

class ReservationTimeServiceTest extends IntegrationTestSupport {

    @Autowired
    private ReservationTimeService target;

    @Test
    @DisplayName("신규 예약 시간을 생성할 수 있다.")
    void createReservationTime() {
        ReservationTimeRequest request = new ReservationTimeRequest("16:00");

        assertThatCode(() -> target.createReservationTime(request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("중복되는 예약 시간을 생성할 수 없다.")
    void duplicated() {
        ReservationTimeRequest request = new ReservationTimeRequest("10:00");

        assertThatThrownBy(() -> target.createReservationTime(request))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 예약 시간이 존재합니다.");
    }

    @Test
    @DisplayName("예약 시간을 사용하는 예약이 존재하면, 삭제하지 않는다.")
    void cantDelete() {
        Long id = 1L;

        assertThatThrownBy(() -> target.deleteReservationTime(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 시간을 사용하고 있는 예약이 존재합니다.");
    }
}
