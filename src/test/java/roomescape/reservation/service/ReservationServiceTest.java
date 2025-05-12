package roomescape.reservation.service;

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
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.member.domain.Member;
import roomescape.reservation.controller.dto.CreateUserReservationRequest;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.service.dto.CreateReservationServiceRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationServiceTest {

    private static final LocalDate FUTURE_DATE = LocalDate.of(30000, 1, 1);

    @Autowired
    private ReservationService reservationService;

    @Test
    @DisplayName("예약 정보를 저장한 다음 저장 정보를 리턴한다")
    void addReservationTest() {
        // given // when
        final CreateReservationServiceRequest creation = new CreateReservationServiceRequest(FUTURE_DATE, 1L, 1L, 2L);
        final Reservation actual = reservationService.addReservation(creation);

        // then
        assertAll(
                () -> assertThat(reservationService.findAllReservations()).hasSize(4),
                () -> assertThat(actual.getId()).isEqualTo(4L)
        );
    }

    @Test
    @DisplayName("테마, 날짜, 시간이 같은 예약이 존재하면 예외를 던진다")
    void addReservationTest_WhenDuplicatedReservationExists() {
        // given
        final CreateUserReservationRequest request = new CreateUserReservationRequest(FUTURE_DATE, 1L, 1L);
        final Member member = Member.of(2L, "USER", "사용자", "user@email.com", "password");
        reservationService.addReservation(request.toCreateReservationServiceRequest(member));

        final CreateReservationServiceRequest duplicated = new CreateReservationServiceRequest(FUTURE_DATE, 1L,
                1L, 2L);

        // when // then
        assertThatThrownBy(() -> reservationService.addReservation(duplicated))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessageContaining("이미 예약이 존재하는 시간입니다");
    }

    @Test
    @DisplayName("과거 시점으로 예약하는 경우 예외를 던진다")
    void addReservationTest_WhenDateIsPast() {
        // given
        final LocalDate pastDate = LocalDate.of(2000, 1, 1);

        // when // then
        final CreateReservationServiceRequest past = new CreateReservationServiceRequest(pastDate, 1L, 1L, 2L);
        assertThatThrownBy(() -> reservationService.addReservation(past))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("과거 시점은 예약할 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 timeId인 경우 예외를 던진다")
    void addReservationTest_WhenTimeIdDoesNotExist() {
        // given
        final long notExistTimeId = 1000L;

        // when // then
        assertThatThrownBy(() -> reservationService.addReservation(
                new CreateReservationServiceRequest(FUTURE_DATE, notExistTimeId, 1L, 2L)))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 예약 가능 시간입니다");
    }

    @Test
    @DisplayName("존재하지 않는 themeId 경우 예외를 던진다")
    void addReservationTest_WhenThemeIdDoesNotExist() {
        // given
        final long notExistThemeId = 1000L;

        // when // then
        assertThatThrownBy(() -> reservationService.addReservation(
                new CreateReservationServiceRequest(FUTURE_DATE, 1L, notExistThemeId, 2L)))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 테마 입니다");
    }

    @Test
    @DisplayName("예약을 조회한다")
    void findReservationsTest() {
        // given // when
        final List<Reservation> actual = reservationService.findAllReservations();

        // then
        assertThat(actual).hasSize(3);
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void removeReservationByIdTest() {
        // given // when // then
        assertDoesNotThrow(() -> reservationService.removeReservationById(1L));
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 예외를 던진다")
    void removeReservationByIdTest_WhenReservationDoesNotExist() {
        // given
        final long notExistId = 1000L;

        // when // then
        assertThatThrownBy(() -> reservationService.removeReservationById(notExistId))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 예약입니다");
    }
}
