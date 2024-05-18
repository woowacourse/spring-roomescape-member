package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.dto.reservation.request.UserReservationRequest;
import roomescape.global.exception.ApplicationException;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.repository.TimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceMockTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private TimeRepository timeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("동일한 날짜와 시간과 테마에 예약을 생성하면 예외가 발생한다")
    void duplicateTimeReservationAddFail() {
        // given
        Time time = new Time(1L, LocalTime.of(12, 30));
        Theme theme = new Theme(1L, "테마명", "설명", "썸네일URL");
        Member member = new Member(1L, "ddang", "user", "ddang@google.com", "password");

        when(timeRepository.findById(any())).thenReturn(time);
        when(themeRepository.findById(any())).thenReturn(theme);
        when(memberRepository.findById(any())).thenReturn(member);
        when(reservationRepository.findByTimeIdAndDateThemeId(any(), any(), any()))
                .thenReturn(List.of(new Reservation(LocalDate.now(), time, theme, member)));

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(
                new UserReservationRequest(LocalDate.now().plusDays(1L), 1L, 1L), 1L)
        ).isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("이미 지난 날짜로 예약을 생성하면 예외가 발생한다")
    void beforeDateReservationFail() {
        // given
        Time time = new Time(1L, LocalTime.of(12, 30));
        Theme theme = new Theme(1L, "테마명", "설명", "썸네일URL");
        Member member = new Member(1L, "ddang", "user", "ddang@google.com", "password");
        LocalDate beforeDate = LocalDate.now().minusDays(1L);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(
                new UserReservationRequest(beforeDate, time.getId(), theme.getId()), member.getId())
        ).isInstanceOf(ApplicationException.class);
    }

    @Test
    @DisplayName("현재 날짜가 예약 당일이지만, 이미 지난 시간으로 예약을 생성하면 예외가 발생한다")
    void beforeTimeReservationFail() {
        // given
        LocalTime requestTime = LocalTime.now();
        LocalTime beforeTime = requestTime.minusMinutes(1L);

        Time time = new Time(1L, beforeTime);
        Theme theme = new Theme(1L, "테마명", "설명", "썸네일URL");
        Member member = new Member(1L, "ddang", "user", "ddang@google.com", "password");

        when(timeRepository.findById(any())).thenReturn(time);
        when(themeRepository.findById(any())).thenReturn(theme);
        when(memberRepository.findById(any())).thenReturn(member);

        // when & then
        assertThatThrownBy(() -> reservationService.createReservation(
                new UserReservationRequest(LocalDate.now(), time.getId(), theme.getId()), member.getId())
        ).isInstanceOf(ApplicationException.class);
    }
}
