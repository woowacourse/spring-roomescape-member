package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.request.ReservationRequest;
import roomescape.model.Member;
import roomescape.model.MemberName;
import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.Role;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservedChecker;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private ReservedChecker reservedChecker;
    private roomescape.service.ReservationTimeFinder reservationTimeFinder;
    private roomescape.service.ThemeFinder themeFinder;
    private roomescape.service.MemberFinder memberFinder;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        reservedChecker = mock(ReservedChecker.class);
        reservationTimeFinder = mock(roomescape.service.ReservationTimeFinder.class);
        themeFinder = mock(roomescape.service.ThemeFinder.class);
        memberFinder = mock(roomescape.service.MemberFinder.class);

        reservationService = new ReservationService(
                reservationRepository,
                reservedChecker,
                reservationTimeFinder,
                themeFinder,
                memberFinder
        );
    }

    @Test
    @DisplayName("관리자가 예약을 추가하면 정상적으로 예약이 반환된다")
    void addReservationByAdmin_success() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        LocalTime time = LocalTime.of(14, 0);
        ReservationTime reservationTime = new ReservationTime(1L, time);
        Theme theme = new Theme(2L, "공포", "무섭다", "aaa");
        Member member = new Member(3L, Role.valueOf("USER"), new MemberName("vector"), "abc", "def");

        ReservationRequest request = new ReservationRequest(
                3L,
                date,
                1L,
                2L
        );

        when(reservationTimeFinder.getReservationTimeById(1L))
                .thenReturn(reservationTime);
        when(reservedChecker.contains(date, 1L, 2L))
                .thenReturn(false);
        when(themeFinder.getThemeById(2L))
                .thenReturn(theme);
        when(memberFinder.getMemberById(3L))
                .thenReturn(member);

        Reservation expected = new Reservation(
                null,
                member,
                new ReservationDateTime(date, reservationTime),
                theme
        );
        when(reservationRepository.addReservation(any(Reservation.class)))
                .thenReturn(expected);

        // when
        Reservation actual = reservationService.addReservationByAdmin(request);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("관리자가 과거 예약을 추가하면 예외가 발생한다")
    void addReservationByAdmin_pastDate() {
        // given
        LocalDate date = LocalDate.now().minusDays(1);
        LocalTime time = LocalTime.of(14, 0);
        ReservationTime reservationTime = new ReservationTime(1L, time);
        Theme theme = new Theme(2L, "공포", "무섭다", "aaa");
        Member member = new Member(3L, Role.valueOf("USER"), new MemberName("vector"), "abc", "def");

        ReservationRequest request = new ReservationRequest(
                1L,
                date,
                1L,
                2L
        );

        when(reservationTimeFinder.getReservationTimeById(1L))
                .thenReturn(reservationTime);
        when(reservedChecker.contains(date, 1L, 2L))
                .thenReturn(false);
        when(themeFinder.getThemeById(2L))
                .thenReturn(theme);
        when(memberFinder.getMemberById(1L))
                .thenReturn(member);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservationByAdmin(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("과거 예약은 불가능합니다.");
    }

    @Test
    @DisplayName("관리자가 중복 예약을 추가하면 예외가 발생한다")
    void addReservationByAdmin_duplicate() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationRequest request = new ReservationRequest(
                3L,
                date,
                1L,
                2L
        );
        when(reservationTimeFinder.getReservationTimeById(1L))
                .thenReturn(new ReservationTime(1L, LocalTime.of(14, 0)));
        when(reservedChecker.contains(date, 1L, 2L))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.addReservationByAdmin(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Reservation already exists");
    }

    @Test
    @DisplayName("삭제할 예약이 없으면 예외가 발생한다")
    void deleteReservation_notFound() {
        // given
        when(reservationRepository.deleteReservation(999L)).thenReturn(0);

        // when & then
        assertThatThrownBy(() -> reservationService.deleteReservation(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("삭제할 예약이 존재하지 않습니다.");
    }
}
