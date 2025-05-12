package roomescape.service.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.domain.enums.Role;
import roomescape.dto.admin.AdminReservationRequest;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.exception.reservation.ReservationAlreadyExistsException;
import roomescape.exception.reservation.ReservationNotFoundException;
import roomescape.exception.reservationtime.ReservationTimeNotFoundException;
import roomescape.exception.theme.ThemeNotFoundException;
import roomescape.repository.member.MemberRepository;
import roomescape.repository.reservation.ReservationRepository;
import roomescape.repository.reservationtime.ReservationTimeRepository;
import roomescape.repository.theme.ThemeRepository;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationTimeRepository timeRepository;

    @Mock
    private ThemeRepository themeRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @DisplayName("예약 시간이 존재하지 않으면 예약을 생성할 수 없다")
    @Test
    void timeNotFound() {
        // given
        LocalDate today = LocalDate.now();
        Long timeId = 1L;
        Long themeId = 1L;

        ReservationRequest request = new ReservationRequest(today, timeId, themeId);

        // when
        when(timeRepository.findById(timeId)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> reservationService.create(request, new Member(1L, "슬링키", "이메일", "비밀번호",
                Role.ADMIN)))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @DisplayName("테마가 존재하지 않으면 예약을 생성할 수 없다")
    @Test
    void themeNotFound() {
        // given
        LocalDate today = LocalDate.now();
        long timeId = 1L;
        long themeId = 1L;
        String name = "에드";
        ReservationRequest request = new ReservationRequest(today, timeId, themeId);

        // when
        when(timeRepository.findById(timeId)).thenReturn(
                Optional.of(new ReservationTime(timeId, LocalTime.now().plusHours(1))));

        when(themeRepository.findById(themeId)).thenReturn(Optional.empty());
        //then
        assertThatThrownBy(() -> reservationService.create(request, new Member(1L, "슬링키", "이메일", "비밀번호", Role.ADMIN)))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @DisplayName("이미 예약이 존재하는 날짜, 시간에는 예약을 생성할 수 없다")
    @Test
    void alreadyExists() {
        // given
        LocalDate today = LocalDate.now();
        long timeId = 1L;
        long themeId = 1L;
        String name = "에드";
        ReservationRequest request = new ReservationRequest(today, timeId, themeId);

        // when
        when(timeRepository.findById(timeId)).thenReturn(
                Optional.of(new ReservationTime(timeId, LocalTime.now().plusHours(1))));

        when(themeRepository.findById(themeId)).thenReturn(
                Optional.of(new Theme(themeId, "test", "test", "test")));
        when(reservationRepository.existsByDateAndTime(today, timeId)).thenReturn(true);

        //then
        assertThatThrownBy(() -> reservationService.create(request, new Member(1L, "슬링키", "이메일", "비밀번호", Role.ADMIN)))
                .isInstanceOf(ReservationAlreadyExistsException.class);
    }

    @DisplayName("존재하지 않는 예약을 삭제 시도하면 예외를 발생시킨다")
    @Test
    void delete() {
        // given
        long id = 99L;

        // when
        when(reservationRepository.deleteReservationById(id)).thenReturn(0);

        // then
        assertThatThrownBy(() -> reservationService.deleteById(id))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @DisplayName("관리자가 정상적으로 예약을 생성할 수 있다")
    @Test
    void createByAdminSuccess() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        long timeId = 2L;
        long themeId = 2L;
        long memberId = 2L;

        AdminReservationRequest request = new AdminReservationRequest(date, timeId, themeId, memberId);

        ReservationTime time = new ReservationTime(timeId, LocalTime.of(10, 0));
        Theme theme = new Theme(themeId, "SF 테마", "미래", "url");
        Member member = new Member(memberId, "관리자", "admin@a.com", "pw", roomescape.domain.enums.Role.ADMIN);
        Reservation reservation = new Reservation(99L, date, time, theme, member);

        // when
        when(timeRepository.findById(anyLong())).thenReturn(Optional.of(time));
        when(themeRepository.findById(anyLong())).thenReturn(Optional.of(theme));
        when(memberRepository.findMemberById(anyLong())).thenReturn(Optional.of(member));
        when(reservationRepository.addReservation(any())).thenReturn(reservation);

        // then
        ReservationResponse response = reservationService.createByAdmin(request);

        assertThat(response.id()).isEqualTo(99L);
        assertThat(response.member().name()).isEqualTo("관리자");
        assertThat(response.theme().name()).isEqualTo("SF 테마");
        assertThat(response.date()).isEqualTo(date);
    }
}
