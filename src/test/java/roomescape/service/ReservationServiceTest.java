package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.domain.dto.ReservationRequest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReservationServiceTest {
    private final ReservationService service;

    @Autowired
    public ReservationServiceTest(final ReservationService service) {
        this.service = service;
    }

    @Test
    @DisplayName("themeId가 존재하지 않을 경우 예외를 발생시킨다.")
    void given_reservationRequest_when_saveWithNotExistThemeId_then_throwException() {
        //given
        ReservationRequest reservationRequest = new ReservationRequest("name", LocalDate.parse("2099-01-01"), 1L, 99L);
        //when, then
        assertThatThrownBy(() -> service.create(reservationRequest)).isInstanceOf(IllegalArgumentException.class);
    }
}