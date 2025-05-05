package roomescape.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static roomescape.reservation.exception.ReservationErrorCode.ALREADY_RESERVED;
import static roomescape.reservation.exception.ReservationErrorCode.PAST_RESERVATION;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.THEME_1;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.application.MemberService;
import roomescape.reservation.application.ReservationService;
import roomescape.reservation.application.ThemeService;
import roomescape.reservation.application.TimeService;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationRegistrationPolicy;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.exception.ImpossibleReservationException;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.presentation.dto.request.ReservationRequest;

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

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationService(
                reservationRepository, timeService, themeService, memberService, reservationRegistrationPolicy
        );
    }

    @DisplayName("예약을 정상적으로 등록할 수 있다.")
    @Test
    void registerReservation() {
        // given
        ReservationRequest request = new ReservationRequest(1L, LocalDate.of(2025, 5, 2), 1L);

        Theme theme = Theme.of(1L, "호러 테마", "완전 호러입니다.", "thumbnail.url");
        ReservationTime time = ReservationTime.of(1L, LocalTime.of(10, 0));
        Long memberId = 1L;
        Reservation reservation = Reservation.createNew(memberId, theme, request.date(), time);

        given(themeService.getThemeById(1L)).willReturn(theme);
        given(timeService.getTimeById(1L)).willReturn(time);
        long reservationId = 1L;
        given(reservationRepository.save(reservation)).willReturn(reservationId);

        // when
        reservationService.registerReservationForUser(request, memberId);

        // then
        verify(themeService).getThemeById(1L);
        verify(timeService).getTimeById(1L);
        verify(reservationRepository).existsDuplicatedReservation(request.date(), request.timeId(), request.themeId());
        verify(reservationRegistrationPolicy).validate(any(Reservation.class), eq(false));
        verify(reservationRepository).save(reservation);
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
        )).willReturn(true);

        // when
        doThrow(new ImpossibleReservationException(PAST_RESERVATION.getMessage()))
                .when(reservationRegistrationPolicy)
                .validate(any(Reservation.class), eq(true));

        // then
        assertThatThrownBy(() -> reservationService.registerReservationForUser(request, memberId))
                .isInstanceOf(ImpossibleReservationException.class)
                .hasMessageContaining(PAST_RESERVATION.getMessage());

        verify(reservationRegistrationPolicy).validate(any(Reservation.class), eq(true));
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
