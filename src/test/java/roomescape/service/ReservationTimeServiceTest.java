package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.dto.ReservationTimeRequest;

@SpringBootTest
@Sql(scripts = "/reset_test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class ReservationTimeServiceTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("중복되는 예약 시간을 생성할 수 없다.")
    void cantCreateDuplicateReservationTime() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest("10:00");

        // when, then
        assertThatThrownBy(() -> reservationTimeService.createReservationTime(request))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    @DisplayName("예약 시간을 사용하는 예약이 존재하면, 삭제하지 않는다.")
    void cantDelete() {
        // given
        Long id = 1L;

        // when, then
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTime(id))
                .isInstanceOf(IllegalStateException.class);
    }
}
