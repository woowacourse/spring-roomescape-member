package roomescape.service;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.dto.request.CreateReservationRequest;
import roomescape.controller.dto.response.ReservationResponse;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.service.dto.ReservationCreation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    ReservationService reservationService;

    @Test
    @DisplayName("같은 날짜 및 시간 예약이 존재하지 않을 경우, 예약 정보를 저장한 다음 id를 리턴한다")
    void saveReservation() {
        //given
        LocalDate date = LocalDate.of(2100, 6, 30);

        //when
        ReservationCreation creation = new ReservationCreation("test", date, 1L, 1L);
        ReservationResponse actual = reservationService.addReservation(creation);

        //then
        assertAll(
                () -> assertThat(reservationService.findAll()).hasSize(2),
                () -> assertThat(actual.id()).isEqualTo(2L)
        );

    }

    @Test
    @DisplayName("같은 날짜 및 시간 예약이 존재하면 -1을 리턴한다")
    void exceptionWhenSameDateTime() {
        //given
        LocalDate date = LocalDate.of(2100, 1, 1);
        reservationService.addReservation(ReservationCreation.from(new CreateReservationRequest("test", date, 1, 1)));

        //when & then
        ReservationCreation duplicated = new ReservationCreation("test", date, 1L, 1L);
        assertThatThrownBy(() -> reservationService.addReservation(duplicated))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessageContaining("이미 예약이 존재하는 시간입니다");
    }

    @Test
    @DisplayName("존재하는 예약을 삭제하면 true를 리턴한다")
    void removeReservationById() {
        //given
        //when
        //then
        assertDoesNotThrow(() -> reservationService.removeReservationById(1L));
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 예외를 던진다")
    void removeNotExistReservationById() {
        //given
        long notExistId = 1000L;
//when & then
        assertThatThrownBy(() -> reservationService.removeReservationById(notExistId))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 예약입니다");
    }

    @Test
    @DisplayName("존재하지 않는 timeId인 경우 예외를 던진다")
    void throwExceptionWhenNotExistTimeId() {
        //given
        //when & then
        assertThatThrownBy(() -> reservationService.addReservation(
                new ReservationCreation("test", LocalDate.of(3000, 1, 1), 1000, 1)))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 예약 가능 시간입니다");
    }
}
