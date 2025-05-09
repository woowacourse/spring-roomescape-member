package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.dto.request.CreateReservationRequest;
import roomescape.domain.LoginMember;
import roomescape.domain.MemberRoleType;
import roomescape.exception.custom.BusinessRuleViolationException;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.NotFoundValueException;
import roomescape.service.dto.request.ReservationCreation;
import roomescape.service.dto.response.ReservationResult;

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
    @DisplayName("예약 정보를 저장한 다음 저장 정보를 리턴한다")
    void saveReservation() {
        //given
        LocalDate date = LocalDate.of(2100, 6, 30);

        //when
        ReservationCreation creation = new ReservationCreation(1L, date, 1L, 1L);
        ReservationResult actual = reservationService.addReservation(creation);

        //then
        assertAll(
                () -> assertThat(reservationService.getAllReservations()).hasSize(2),
                () -> assertThat(actual.id()).isEqualTo(2L)
        );
    }

    @Test
    @DisplayName("테마, 날짜, 시간이 같은 예약이 존재하면 예외를 던진다")
    void exceptionWhenSameDateTime() {
        //given
        LocalDate date = LocalDate.of(2100, 1, 1);
        ReservationCreation creation = ReservationCreation.of(
                new LoginMember(1, "test", "test@email.com", MemberRoleType.MEMBER),
                new CreateReservationRequest(date, 1, 1));
        reservationService.addReservation(creation);

        //when & then
        assertThatThrownBy(() -> reservationService.addReservation(creation))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessageContaining("이미 예약이 존재하는 시간입니다");
    }

    @Test
    @DisplayName("과거 시점으로 예약하는 경우 예외를 던진다")
    void exceptionWhenPastDate() {
        // given
        LocalDate date = LocalDate.of(2000, 1, 1);

        //when & then
        ReservationCreation past = ReservationCreation.of(
                new LoginMember(1, "test", "test@email.com", MemberRoleType.MEMBER),
                new CreateReservationRequest(date, 1, 1));

        assertThatThrownBy(() -> reservationService.addReservation(past))
                .isInstanceOf(BusinessRuleViolationException.class)
                .hasMessageContaining("과거 시점은 예약할 수 없습니다");
    }

    @Test
    @DisplayName("존재하지 않는 timeId인 경우 예외를 던진다")
    void throwExceptionWhenNotExistTimeId() {
        //given
        ReservationCreation notValidTimeId = ReservationCreation.of(
                new LoginMember(1, "test", "test@email.com", MemberRoleType.MEMBER),
                new CreateReservationRequest(LocalDate.of(3000, 1, 1), 1000, 1));

        // when & then
        assertThatThrownBy(() -> reservationService.addReservation(notValidTimeId))
                .isInstanceOf(NotFoundValueException.class)
                .hasMessageContaining("존재하지 않는 예약 가능 시간입니다");
    }

    @Test
    @DisplayName("존재하지 않는 themeId 경우 예외를 던진다")
    void throwExceptionWhenNotExistThemeId() {
        //given
        ReservationCreation notValidThemeId = ReservationCreation.of(
                new LoginMember(1, "test", "test@email.com", MemberRoleType.MEMBER),
                new CreateReservationRequest(LocalDate.of(3000, 1, 1), 1, 1000));

        //when & then
        assertThatThrownBy(() -> reservationService.addReservation(notValidThemeId))
                .isInstanceOf(NotFoundValueException.class)
                .hasMessageContaining("존재하지 않는 테마 입니다");
    }

    @Test
    @DisplayName("예약을 조회한다")
    void findReservation() {
        //given //when
        List<ReservationResult> actual = reservationService.getAllReservations();

        //then
        assertThat(actual).hasSize(1);
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void removeReservationById() {
        //given //when //then
        assertDoesNotThrow(() -> reservationService.removeReservationById(1L));
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 예외를 던진다")
    void removeNotExistReservationById() {
        //given
        long notExistId = 1000L;
        //when & then
        assertThatThrownBy(() -> reservationService.removeReservationById(notExistId))
                .isInstanceOf(NotFoundValueException.class)
                .hasMessageContaining("존재하지 않는 예약입니다");
    }
}
