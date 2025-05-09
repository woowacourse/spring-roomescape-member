package roomescape.reservation.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static roomescape.reservation.exception.ReservationErrorCode.ALREADY_RESERVED;
import static roomescape.reservation.exception.ReservationErrorCode.PAST_RESERVATION;
import static roomescape.testFixture.Fixture.MEMBER_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.THEME_1;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.NotFoundException;
import roomescape.member.application.MemberService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRegistrationPolicy;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.exception.ImpossibleReservationException;
import roomescape.reservation.presentation.dto.request.AdminReservationRequest;
import roomescape.reservation.presentation.dto.request.ReservationRequest;
import roomescape.theme.application.ThemeService;
import roomescape.time.application.TimeService;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TimeService timeService;

    @Mock
    private ThemeService themeService;

    @Mock
    private MemberService memberService;

    @Mock
    private ReservationRegistrationPolicy reservationRegistrationPolicy;

    @InjectMocks
    private ReservationService reservationService;

    @DisplayName("관리자는 예약을 등록할 수 있다.")
    @Test
    void registerReservationForAdmin() {
        // given
        AdminReservationRequest request = new AdminReservationRequest(LocalDate.of(2025, 6, 1), 1L, 1L, 1L);

        given(memberService.existsById(1L)).willReturn(true);
        given(themeService.getThemeById(1L)).willReturn(THEME_1);
        given(timeService.getTimeById(1L)).willReturn(RESERVATION_TIME_1);
        given(reservationRepository.save(any(Reservation.class))).willReturn(1L);

        // when
        reservationService.registerReservationForAdmin(request);

        // then
        verify(memberService).existsById(MEMBER_1.getId());
        verify(themeService).getThemeById(THEME_1.getId());
        verify(timeService).getTimeById(RESERVATION_TIME_1.getId());
        verify(reservationRepository).existsDuplicatedReservation(request.date(), request.timeId(), request.themeId());
        verify(reservationRegistrationPolicy).validate(any(Reservation.class), eq(false));
        verify(reservationRepository).save(any(Reservation.class));
    }

    @DisplayName("사용자는 자신의 id를 통해 예약을 정상적으로 등록할 수 있다.")
    @Test
    void registerReservation() {
        // given
        ReservationRequest request = new ReservationRequest(1L, LocalDate.of(2025, 5, 2), 1L);

        given(themeService.getThemeById(1L)).willReturn(THEME_1);
        given(timeService.getTimeById(1L)).willReturn(RESERVATION_TIME_1);
        given(reservationRepository.save(any(Reservation.class))).willReturn(1L);

        // when
        Long memberId = 1L;
        reservationService.registerReservationForUser(request, memberId);

        // then
        verify(themeService).getThemeById(1L);
        verify(timeService).getTimeById(1L);
        verify(reservationRepository).existsDuplicatedReservation(request.date(), request.timeId(), request.themeId());
        verify(reservationRegistrationPolicy).validate(any(Reservation.class), eq(false));
        verify(reservationRepository).save(any(Reservation.class));
    }

    @DisplayName("중복된 예약일 때 도메인 예외를 그대로 전파한다")
    @Test
    void throwSameExceptionWithDomain_when_duplicatedReservation() {
        // given
        ReservationRequest request = new ReservationRequest(1L, LocalDate.of(2025, 1, 1), 1L);
        Long memberId = 1L;
        given(themeService.getThemeById(1L)).willReturn(THEME_1);
        given(timeService.getTimeById(1L)).willReturn(RESERVATION_TIME_1);
        given(reservationRepository.existsDuplicatedReservation(
                request.date(), request.timeId(), request.themeId()
        )).willReturn(true);

        doThrow(new ImpossibleReservationException(ALREADY_RESERVED.getMessage()))
                .when(reservationRegistrationPolicy)
                .validate(any(Reservation.class), eq(true));

        // when & then
        assertThatThrownBy(() -> reservationService.registerReservationForUser(request, memberId))
                .isInstanceOf(ImpossibleReservationException.class)
                .hasMessage(ALREADY_RESERVED.getMessage());

        verify(reservationRegistrationPolicy).validate(any(Reservation.class), eq(true));
    }

    @DisplayName("과거 일시 예약일 때 도메인 예외를 그대로 전파한다")
    @Test
    void throwSameExceptionWithDomain_when_pastDate() {
        // given
        LocalDate pastDate = LocalDate.now().minusDays(1);
        ReservationRequest request = new ReservationRequest(1L, pastDate, 1L);
        Long memberId = 1L;

        given(themeService.getThemeById(1L)).willReturn(THEME_1);
        given(timeService.getTimeById(1L)).willReturn(RESERVATION_TIME_1);
        given(reservationRepository.existsDuplicatedReservation(
                request.date(), request.timeId(), request.themeId()
        )).willReturn(false);

        // when
        doThrow(new ImpossibleReservationException(PAST_RESERVATION.getMessage()))
                .when(reservationRegistrationPolicy)
                .validate(any(Reservation.class), eq(false));

        // then
        assertThatThrownBy(() -> reservationService.registerReservationForUser(request, memberId))
                .isInstanceOf(ImpossibleReservationException.class)
                .hasMessageContaining(PAST_RESERVATION.getMessage());

        verify(reservationRegistrationPolicy).validate(any(Reservation.class), eq(false));
    }

    @DisplayName("존재하지 않는 themeId로 예약 시 NotFoundException이 발생한다.")
    @Test
    void registerReservation_fail_when_themeNotFound() {
        // given
        ReservationRequest request = new ReservationRequest(999L, LocalDate.now(), 1L);
        Long memberId = 1L;

        given(themeService.getThemeById(999L)).willThrow(new NotFoundException("theme", 999L));

        // when & then
        assertThatThrownBy(() -> reservationService.registerReservationForUser(request, memberId))
                .isInstanceOf(NotFoundException.class);
    }

    @DisplayName("예약을 삭제할 수 있다.")
    @Test
    void deleteReservationById() {
        // given
        Long id = 1L;

        // when
        reservationService.deleteReservation(id);

        // then
        verify(reservationRepository).deleteById(id);
    }
}
