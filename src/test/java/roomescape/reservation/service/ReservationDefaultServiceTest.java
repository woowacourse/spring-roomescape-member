package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.dto.UserReservationRequest;
import roomescape.reservation.exception.ReservationAlreadyExistsException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationDefaultServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository timeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReservationDefaultService reservationService;

    @DisplayName("예약 시간이 존재하지 않으면 예약을 생성할 수 없다")
    @Test
    void timeNotFound() {
        // given
        LocalDate today = LocalDate.now();
        long timeId = 1L;
        long themeId = 1L;
        long memberId = 1L;
        UserReservationRequest request = new UserReservationRequest(today, timeId, themeId);

        // when
        when(memberRepository.findById(memberId)).thenReturn(
                Optional.of(Member.createWithId(memberId, "에드", "email", "password", Role.USER)));
        when(timeRepository.findById(timeId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> reservationService.createForUser(request, memberId))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @DisplayName("테마가 존재하지 않으면 예약을 생성할 수 없다")
    @Test
    void themeNotFound() {
        // given
        LocalDate today = LocalDate.now();
        long timeId = 1L;
        long themeId = 1L;
        long memberId = 1L;
        UserReservationRequest request = new UserReservationRequest(today, timeId, themeId);

        // when
        when(memberRepository.findById(memberId)).thenReturn(
                Optional.of(Member.createWithId(memberId, "에드", "email", "password", Role.USER)));
        when(timeRepository.findById(timeId)).thenReturn(
                Optional.of(ReservationTime.createWithId(timeId, LocalTime.now().plusHours(1))));

        when(themeRepository.findById(themeId)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> reservationService.createForUser(request, memberId))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @DisplayName("이미 예약이 존재하는 날짜, 시간에는 예약을 생성할 수 없다")
    @Test
    void alreadyExists() {
        // given
        LocalDate today = LocalDate.now();
        long timeId = 1L;
        long themeId = 1L;
        long memberId = 1L;
        UserReservationRequest request = new UserReservationRequest(today, timeId, themeId);

        // when
        when(memberRepository.findById(memberId)).thenReturn(
                Optional.of(Member.createWithId(memberId, "에드", "email", "password", Role.USER)));
        when(timeRepository.findById(timeId)).thenReturn(
                Optional.of(ReservationTime.createWithId(timeId, LocalTime.now().plusHours(1))));

        when(themeRepository.findById(themeId)).thenReturn(
                Optional.of(Theme.createWithId(themeId, "test", "test", "test")));
        when(reservationRepository.existsByDateAndTime(today, timeId)).thenReturn(true);

        //then
        assertThatThrownBy(() -> reservationService.createForUser(request, memberId))
                .isInstanceOf(ReservationAlreadyExistsException.class);
    }

    @DisplayName("존재하지 않는 예약을 삭제 시도하면 예외를 발생시킨다")
    @Test
    void delete() {
        // given
        long id = 99L;

        // when
        when(reservationRepository.deleteById(id)).thenReturn(0);

        // then
        assertThatThrownBy(() -> reservationService.deleteById(id))
                .isInstanceOf(ReservationNotFoundException.class);
    }
}
