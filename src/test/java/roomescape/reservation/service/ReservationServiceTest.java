package roomescape.reservation.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.controller.dto.AvailableTimeResponse;
import roomescape.time.controller.dto.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.util.repository.MemberFakeRepository;
import roomescape.util.repository.ReservationFakeRepository;
import roomescape.util.repository.ReservationTimeFakeRepository;
import roomescape.util.repository.ThemeFakeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void setup() {
        ReservationRepository reservationRepository = new ReservationFakeRepository();
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
        ThemeRepository themeRepository = new ThemeFakeRepository(reservationRepository);
        MemberRepository memberRepository = new MemberFakeRepository();

        List<ReservationTime> times = List.of(
                new ReservationTime(null, LocalTime.of(3, 12)),
                new ReservationTime(null, LocalTime.of(11, 33)),
                new ReservationTime(null, LocalTime.of(16, 54)),
                new ReservationTime(null, LocalTime.of(23, 53))
        );

        List<Theme> themes = List.of(
                new Theme(null, "레벨1 탈출", "우테코 레벨1를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new Theme(null, "레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"),
                new Theme(null, "레벨3 탈출", "우테코 레벨3를 탈출하는 내용입니다.",
                        "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        for (Theme theme : themes) {
            themeRepository.saveAndReturnId(theme);
        }

        for (ReservationTime time : times) {
            reservationTimeRepository.saveAndReturnId(time);
        }

        Member member = new Member(1L, "a", "a", "하루", Role.USER);
        memberRepository.saveAndReturnId(member);

        List<Reservation> reservations = List.of(
                new Reservation(null, member, LocalDate.of(2025, 3, 28), reservationTimeRepository.findById(1L).get(),
                        themeRepository.findById(1L).get()),
                new Reservation(null, member, LocalDate.of(2025, 4, 5), reservationTimeRepository.findById(2L).get(),
                        themeRepository.findById(2L).get()),
                new Reservation(null, member, LocalDate.of(2025, 5, 15), reservationTimeRepository.findById(3L).get(),
                        themeRepository.findById(3L).get())
        );

        for (Reservation reservation : reservations) {
            reservationRepository.saveAndReturnId(reservation);
        }

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository,
                memberRepository);
    }

    @DisplayName("전체 예약 정보를 조회한다")
    @Test
    void get_all_test() {
        // when
        List<ReservationResponse> reservationResponses = reservationService.getAll();

        // then
        assertThat(reservationResponses).hasSize(3);
    }

    @DisplayName("예약 정보를 추가한다")
    @Test
    void add_test() {
        // given
        ReservationRequest request = new ReservationRequest(LocalDate.now(), 4L, 3L, 1L);

        // when
        ReservationResponse response = reservationService.add(request);

        // then
        ReservationTimeResponse expectedTimeResponse = new ReservationTimeResponse(4L, LocalTime.of(23, 53));
        ThemeResponse expectedThemeResponse = new ThemeResponse(3L, "레벨3 탈출", "우테코 레벨3를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        MemberResponse expectedMemberResponse = new MemberResponse(1L, "하루");
        ReservationResponse expected = new ReservationResponse(
                4L,
                expectedMemberResponse,
                LocalDate.now(),
                expectedTimeResponse,
                expectedThemeResponse
        );
        assertThat(response).isEqualTo(expected);
    }

    @DisplayName("성공적으로 예약 정보를 삭제하면 예외가 발생하지 않는다")
    @Test
    void remove_test() {
        // given
        Long removeId = 3L;

        // when & then
        assertThatCode(() -> reservationService.remove(removeId))
                .doesNotThrowAnyException();
    }

    @DisplayName("지난 날짜인 경우 예외가 발생한다")
    @Test
    void past_day_exception_test() {
        // given
        ReservationRequest request = new ReservationRequest(LocalDate.now().minusDays(1), 4L, 3L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.add(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 날짜는 예약할 수 없습니다.");
    }

    @DisplayName("지난 시각인 경우 예외가 발생한다")
    @Test
    void past_time_exception_test() {
        // given
        ReservationRequest request = new ReservationRequest(LocalDate.now(), 1L, 3L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.add(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 시각은 예약할 수 없습니다.");
    }

    @DisplayName("미래 날짜인데 시간이 현재보다 과거인 경우 예외가 발생하지 않는다")
    @Test
    void future_test() {
        // given
        ReservationRequest request = new ReservationRequest(LocalDate.now().plusDays(3), 1L, 1L, 1L);

        // when & then
        assertThatCode(() -> reservationService.add(request))
                .doesNotThrowAnyException();
    }

    @DisplayName("동일한 날짜, 시간, 테마에 대한 중복 예약을 하면 예외가 발생한다")
    @Test
    void reservation_duplicate_exception() {
        // given
        ReservationRequest request = new ReservationRequest(LocalDate.of(2025, 5, 15), 3L, 3L, 1L);

        // when & then
        assertThatThrownBy(() -> reservationService.add(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 날짜, 시간, 테마에 대한 동일한 예약이 존재합니다.");
    }

    @DisplayName("날짜, 테마 ID에 해당하는 예약 가능 시간 정보를 반환한다")
    @Test
    void get_available_times_test() {
        // given
        LocalDate date = LocalDate.of(2025, 3, 28);
        Long themeId = 1L;

        // when
        List<AvailableTimeResponse> availableTimes = reservationService.getAvailableTimes(date, themeId);

        // then
        Assertions.assertThat(availableTimes.get(0).alreadyBooked()).isTrue();
        Assertions.assertThat(availableTimes.get(1).alreadyBooked()).isFalse();
    }

}
