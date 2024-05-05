package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ReservationTimeCreateRequest;
import roomescape.dto.ReservationTimeResponse;
import roomescape.exception.NotExistingEntryException;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Test
    @DisplayName("예약 시간 생성 기능을 확인한다.")
    void checkReservationTimeCreate() {
        //given
        LocalTime time = LocalTime.parse("10:00");
        ReservationTimeCreateRequest reservationTimeCreateRequest = new ReservationTimeCreateRequest(time);

        //when
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(reservationTimeCreateRequest);

        //then
        assertAll(
                () -> assertThat(reservationTimeResponse.id()).isEqualTo(1L),
                () -> assertThat(reservationTimeResponse.startAt()).isEqualTo("10:00")
        );
    }

    @Test
    @DisplayName("예약 시간 삭제 기능을 확인한다.")
    void checkReservationTimeDelete() {
        //given
        LocalTime time = LocalTime.parse("10:00");
        ReservationTimeCreateRequest reservationTimeCreateRequest = new ReservationTimeCreateRequest(time);
        ReservationTimeResponse reservationTimeResponse = reservationTimeService.create(reservationTimeCreateRequest);

        //when & then
        assertDoesNotThrow(() -> reservationTimeService.delete(reservationTimeResponse.id()));
    }

    @Test
    @DisplayName("예약 시간 삭제 기능의 실패를 확인한다.")
    void checkReservationTimeDeleteFail() {
        //given & when & then
        assertThatThrownBy(() -> reservationTimeService.delete(0L))
                .isInstanceOf(NotExistingEntryException.class)
                .hasMessage("삭제할 예약 시간이 존재하지 않습니다");
    }
}
