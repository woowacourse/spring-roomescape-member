package roomescape.time.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.controller.dto.ReservationTimeRequest;
import roomescape.time.controller.dto.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeRepository;
import roomescape.util.repository.ReservationFakeRepository;
import roomescape.util.repository.ReservationTimeFakeRepository;
import roomescape.util.repository.ThemeFakeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setup() {
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
        ReservationRepository reservationRepository = new ReservationFakeRepository();
        ThemeRepository themeRepository = new ThemeFakeRepository(reservationRepository);

        List<ReservationTime> times = List.of(
                new ReservationTime(null, LocalTime.of(3, 12)),
                new ReservationTime(null, LocalTime.of(11, 33)),
                new ReservationTime(null, LocalTime.of(16, 54)),
                new ReservationTime(null, LocalTime.of(23, 53))
        );

        for (ReservationTime time : times) {
            reservationTimeRepository.saveAndReturnId(time);
        }

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

        Member member = new Member(1L, "a", "a", "하루", Role.USER);
        Reservation reservation = new Reservation(null, member, LocalDate.of(2025, 4, 29),
                reservationTimeRepository.findById(1L).get(), themeRepository.findById(1L).get());

        reservationRepository.saveAndReturnId(reservation);

        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
    }

    @DisplayName("예약 시간 정보를 추가한다")
    @Test
    void add_test() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(17, 25));

        // when
        ReservationTimeResponse response = reservationTimeService.add(request);

        // then
        ReservationTimeResponse expected = new ReservationTimeResponse(5L, LocalTime.of(17, 25));
        assertThat(response).isEqualTo(expected);
    }

    @DisplayName("예약 시간 정보 정상적으로 삭제하면 예외가 발생하지 않는다")
    @Test
    void remove_test() {
        // given
        Long removeId = 3L;

        // when & then
        assertThatCode(() -> reservationTimeService.remove(removeId))
                .doesNotThrowAnyException();
    }

    @DisplayName("예약 정보 시간을 모두 조회한다")
    @Test
    void get_times_test() {
        // when
        List<ReservationTimeResponse> reservationTimeResponse = reservationTimeService.getTimes();

        // then
        assertThat(reservationTimeResponse).hasSize(4);
    }

    @DisplayName("특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하면 예외가 발생한다")
    @Test
    void delete_exception() {
        // given
        Long deleteId = 1L;

        // when & then
        assertThatThrownBy(() -> reservationTimeService.remove(deleteId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("해당 시간에 대한 예약이 존재합니다.");
    }

    @DisplayName("동일한 시간을 생성하는 경우 예외가 발생한다")
    @Test
    void duplicate_time_test() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(3, 12));

        // when & then
        assertThatThrownBy(() -> reservationTimeService.add(reservationTimeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간은 이미 존재합니다.");
    }

}
