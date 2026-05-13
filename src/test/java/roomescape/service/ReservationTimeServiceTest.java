package roomescape.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {
    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("없는 시간을 삭제하면 에러가 발생한다.")
    void 없는_시간_삭제_에러_테스트() {
        Long timeId = 999L;

        assertThatThrownBy(() -> reservationTimeService.delete(timeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 시간입니다.");
    }

    @Test
    @DisplayName("예약이 존재하는 시간을 삭제하면 에러가 발생한다.")
    void 예약_존재_시간_삭제_에러_테스트() {
        Long timeId = 1L;

        assertThatThrownBy(() -> reservationTimeService.delete(timeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("예약이 존재하는 시간은 삭제할 수 없습니다.");
    }
}
