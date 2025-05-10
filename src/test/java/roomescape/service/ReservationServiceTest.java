package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.reservation.ReservationDao;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationCreateRequest;
import roomescape.dto.request.ReservationSearchRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.ReservationDuplicateException;

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
        final ReservationTime time = new ReservationTime(1L, LocalTime.now().plusHours(1));
        final Theme theme = new Theme(1L, "test", "테마1", "설명1");
        final ReservationCreateRequest request = new ReservationCreateRequest(LocalDate.now(), 1L, 1L);
        final Member member = new Member(1L, "test", "test", "test", MemberRole.USER);
        when(reservationTimeService.findById(1L)).thenReturn(time);
        when(themeService.findById(1L)).thenReturn(theme);
        when(reservationDao.findByThemeAndDateAndTime(any(Reservation.class))).thenReturn(Optional.of(Reservation.load(
                1L, LocalDate.now(), time, theme, member)));

        // when & then
        assertThatThrownBy(() -> reservationService.create(request, member))
                .isInstanceOf(ReservationDuplicateException.class)
                .hasMessage("이미 존재하는 예약입니다.");
    }

    @DisplayName("테마와 멤버 날짜로 예약 목록을 조회한다.")
    @Test
    void findThemeAndMemberAndDateTest() {

        // given
        final Reservation reservation = Reservation.load(1L, LocalDate.of(2025, 4, 25),
                new ReservationTime(1L, LocalTime.of(10, 10)), new Theme(1L, "test", "test", "test"),
                new Member(1L, "test", "test@email.com", "password", MemberRole.USER));
        final List<Reservation> reservations = List.of(reservation);
        final ReservationSearchRequest reservationSearchRequest = new ReservationSearchRequest(1L, 1L,
                LocalDate.of(2025, 4, 25), LocalDate.of(2025, 4, 28));

        // when
        when(reservationDao.findByThemeAndMemberAndDate(1L, 1L, LocalDate.of(2025, 4, 25),
                LocalDate.of(2025, 4, 28))).thenReturn(reservations);
        final List<ReservationResponse> reservationResponses = reservationService.findByThemeAndMemberAndDate(
                reservationSearchRequest);

        // then
        assertThat(reservationResponses.size()).isEqualTo(1);

    }
}
