package roomescape.reservation.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.infrastructure.MemberRepository;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.MemberReservationRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.infrastructure.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.infrastructure.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.infrastructure.TimeRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class ReservationServiceTest {
    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private TimeRepository timeRepository;
    private ThemeRepository themeRepository;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        timeRepository = mock(TimeRepository.class);
        themeRepository = mock(ThemeRepository.class);
        memberRepository = mock(MemberRepository.class);
        reservationService = new ReservationService(reservationRepository, timeRepository, themeRepository, memberRepository);
    }

    @Test
    @DisplayName("유효한 예약 요청이 주어지면 예약이 성공적으로 추가된다")
    void test1() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Long timeId = 1L;
        Long themeId = 2L;

        MemberReservationRequest request = new MemberReservationRequest(date, timeId, themeId);
        Time time = mock(Time.class);
        Theme theme = mock(Theme.class);

        given(timeRepository.findById(timeId)).willReturn(Optional.of(time));
        given(themeRepository.findById(themeId)).willReturn(Optional.of(theme));
        given(reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)).willReturn(false);

        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);
        Reservation saved = new Reservation(1L, member, date, time, theme);
        given(reservationRepository.add(any())).willReturn(saved);

        // when
        ReservationResponse response = reservationService.addByUser(request, member);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("이미 동일한 시간과 테마에 대한 예약이 존재하면 예외가 발생한다")
    void test2() {
        // given
        LocalDate date = LocalDate.now().plusDays(1);
        Long timeId = 1L;
        Long themeId = 2L;

        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);
        MemberReservationRequest request = new MemberReservationRequest(date, timeId, themeId);
        given(timeRepository.findById(timeId)).willReturn(Optional.of(mock(Time.class)));
        given(themeRepository.findById(themeId)).willReturn(Optional.of(mock(Theme.class)));
        given(reservationRepository.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.addByUser(request, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 예약이 존재합니다.");
    }

    @Test
    @DisplayName("오늘 날짜이고 선택한 시간이 현재 시간보다 이전이면 예외가 발생한다")
    void test3() {
        // given
        LocalDate today = LocalDate.now();
        Long timeId = 1L;
        Long themeId = 2L;

        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);
        MemberReservationRequest request = new MemberReservationRequest(today, timeId, themeId);
        Time time = mock(Time.class);

        given(timeRepository.findById(timeId)).willReturn(Optional.of(time));
        given(themeRepository.findById(themeId)).willReturn(Optional.of(mock(Theme.class)));
        given(reservationRepository.existsByDateAndTimeIdAndThemeId(today, timeId, themeId)).willReturn(false);
        given(time.isBefore(any(LocalTime.class))).willReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.addByUser(request, member))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 지난 시간으로는 예약할 수 없습니다.");
    }

    @Test
    @DisplayName("예약 목록 조회 시 모든 예약이 반환된다")
    void test4() {
        // given
        Theme theme = new Theme(1L, "테마 이름", "테마 설명", "테마 URL");
        Time time = new Time(2L, LocalTime.of(14, 0));
        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);
        Reservation reservation = new Reservation(1L, member, LocalDate.of(2025, 5, 1), time, theme);

        given(reservationRepository.findAll()).willReturn(List.of(reservation));

        // when
        List<ReservationResponse> result = reservationService.findAll();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
        assertThat(result.get(0).theme().id()).isEqualTo(1L);
        assertThat(result.get(0).member().id()).isEqualTo(1L);
        assertThat(result.get(0).member().name()).isEqualTo("미미");
    }

    @Test
    @DisplayName("예약 id가 존재할 경우 예약을 성공적으로 삭제한다")
    void test5() {
        // given
        Long existedId = 1L;
        given(reservationRepository.findById(existedId)).willReturn(Optional.of(mock(Reservation.class)));

        // when
        reservationService.deleteById(existedId);

        // then
        then(reservationRepository).should().deleteById(existedId);
    }

    @Test
    @DisplayName("존재하지 않는 id로 삭제를 요청할 시 예외가 발생한다")
    void test6() {
        // given
        Long notExistedId = 999L;
        given(reservationRepository.findById(notExistedId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> reservationService.deleteById(notExistedId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 해당 id의 예약이 존재하지 않습니다.");
    }
}
