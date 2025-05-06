package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.dto.request.CreateReservationRequest;
import roomescape.domain.reservation.Reservation;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.service.dto.ReservationCreation;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    private static final LocalDate FUTURE_DATE = LocalDate.of(30000, 1, 1);

    @Autowired
    ReservationService reservationService;

    @Test
    @DisplayName("예약 정보를 저장한 다음 저장 정보를 리턴한다")
    void saveReservation() {
        // given // when
        final ReservationCreation creation = new ReservationCreation("검프", FUTURE_DATE, 1L, 1L);
        final Reservation actual = reservationService.addReservation(creation);

        // then
        assertAll(
                () -> assertThat(reservationService.findAllReservations()).hasSize(4),
                () -> assertThat(actual.getId()).isEqualTo(4L)
        );
    }

    @Test
    @DisplayName("테마, 날짜, 시간이 같은 예약이 존재하면 예외를 던진다")
    void exceptionWhenSameDateTime() {
        // given
        reservationService.addReservation(ReservationCreation.from(
                new CreateReservationRequest("test", FUTURE_DATE, 1L, 1L)));

        final ReservationCreation duplicated = new ReservationCreation("test", FUTURE_DATE, 1L, 1L);

        // when // then
        assertThatThrownBy(() -> reservationService.addReservation(duplicated))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessageContaining("이미 예약이 존재하는 시간입니다");
    }

    @Test
    @DisplayName("과거 시점으로 예약하는 경우 예외를 던진다")
    void exceptionWhenPastDate() {
        // given
        final LocalDate pastDate = LocalDate.of(2000, 1, 1);

        // when // then
        final ReservationCreation past = new ReservationCreation("test", pastDate, 1L, 1L);
        assertThatThrownBy(() -> reservationService.addReservation(past))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("과거 시점은 예약할 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 timeId인 경우 예외를 던진다")
    void throwExceptionWhenNotExistTimeId() {
        // given
        final long notExistTimeId = 1000L;

        // when // then
        assertThatThrownBy(() -> reservationService.addReservation(
                new ReservationCreation("test", FUTURE_DATE, notExistTimeId, 1L)))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 예약 가능 시간입니다");
    }

    @Test
    @DisplayName("존재하지 않는 themeId 경우 예외를 던진다")
    void throwExceptionWhenNotExistThemeId() {
        // given
        final long notExistThemeId = 1000L;

        // when // then
        assertThatThrownBy(() -> reservationService.addReservation(
                new ReservationCreation("test", FUTURE_DATE, 1L, notExistThemeId)))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 테마 입니다");
    }

    @Test
    @DisplayName("예약을 조회한다")
    void findReservation() {
        // given // when
        final List<Reservation> actual = reservationService.findAllReservations();

        // then
        assertThat(actual).hasSize(3);
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void removeReservationById() {
        // given // when // then
        assertDoesNotThrow(() -> reservationService.removeReservationById(1L));
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 예외를 던진다")
    void removeNotExistReservationById() {
        // given
        final long notExistId = 1000L;

        // when // then
        assertThatThrownBy(() -> reservationService.removeReservationById(notExistId))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 예약입니다");
    }
}
