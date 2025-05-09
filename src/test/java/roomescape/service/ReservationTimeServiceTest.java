package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.DeletionNotAllowedException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeServiceTest {

    @Autowired
    ReservationTimeService service;

    @DisplayName("존재하지 않는 예약시간을 삭제하려는 경우 예외를 던진다")
    @Test
    void removeReservationTime() {
        //given
        long notExistId = 999;

        //when & then
        assertThatThrownBy(() -> service.removeReservationTime(notExistId)).isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 예약 시간 999번에 해당하는 시간이 없습니다.");
    }

    @DisplayName("예약시간과 연결된 예약이 존재하는 경우 예약시간을 삭제할 수 없다.")
    @Test
    void removeReservationTimeWithExistsReservation() {
        // given
        long existId = 1L;

        // when & then
        assertThatThrownBy(() -> service.removeReservationTime(existId)).isInstanceOf(DeletionNotAllowedException.class)
                .hasMessage("[ERROR] 예약이 연결된 시간은 삭제할 수 없습니다. 관련 예약을 먼저 삭제해주세요.");
    }

}
