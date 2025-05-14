package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import roomescape.exception.ConflictException;
import roomescape.exception.ExceptionCause;
import roomescape.exception.NotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.service.MemberService;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.ThemeService;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    ReservationDao reservationDao;

    @Mock
    ReservationTimeService reservationTimeService;

    @Mock
    ThemeService themeService;

    @Mock
    MemberService memberService;

    @InjectMocks
    ReservationService reservationService;

    @DisplayName("날짜와 시간이 중복되는 예약을 생성한다면 예외가 발생한다.")
    @Test
    void createThrowExceptionIfAlreadyExistDateAndTimeTest() {

        // given
        ReservationTime time = ReservationTime.load(1L, LocalTime.now().plusHours(1));
        Theme theme = Theme.load(1L, "test", "테마1", "설명1");
        ReservationCreateRequest request = new ReservationCreateRequest(LocalDate.now(), 1L, 1L);
        Member member = new Member(1L, "test", "test@test.com", "password123!", Role.USER);

        when(reservationTimeService.findById(any(Long.class))).thenReturn(time);
        when(themeService.findById(any(Long.class))).thenReturn(theme);
        when(memberService.findById(any(Long.class))).thenReturn(member);
        when(reservationDao.findByThemeAndDateAndTime(any(Reservation.class))).thenReturn(Optional.of(Reservation.load(
                1L, LocalDate.now(), member, time, theme)));

        // when & then
        assertThatThrownBy(() -> reservationService.create(1L, request))
                .isInstanceOf(ConflictException.class)
                .hasMessage(ExceptionCause.RESERVATION_DUPLICATE.getMessage());
    }

    @DisplayName("존재하지 않는 예약을 삭제 시 예외가 발생한다.")
    @Test
    void deleteTest_NoSuchElementException() {

        // given
        long id = 1L;
        when(reservationDao.existById(id)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> reservationService.delete(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ExceptionCause.RESERVATION_NOTFOUND.getMessage());
    }
}
