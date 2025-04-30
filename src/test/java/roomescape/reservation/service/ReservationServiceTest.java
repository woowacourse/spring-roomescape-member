package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.repository.ReservationFakeRepository;
import roomescape.domain.repository.ReservationTimeFakeRepository;
import roomescape.domain.repository.ThemeFakeRepository;
import roomescape.reservation.controller.dto.ReservationRequest;
import roomescape.reservation.controller.dto.ReservationResponse;
import roomescape.reservation.controller.dto.ReservationTimeResponse;
import roomescape.reservation.controller.dto.ThemeResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservation.domain.repository.ReservationTimeRepository;
import roomescape.reservation.domain.repository.ThemeRepository;

class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void setup() {
        ReservationRepository reservationRepository = new ReservationFakeRepository();
        ReservationTimeRepository reservationTimeRepository = new ReservationTimeFakeRepository();
        ThemeRepository themeRepository = new ThemeFakeRepository();

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

        List<Reservation> reservations = List.of(
                new Reservation(null, "루키", LocalDate.of(2025, 3, 28), reservationTimeRepository.findById(1L), themeRepository.findById(1L).get()),
                new Reservation(null, "슬링키", LocalDate.of(2025, 4, 5), reservationTimeRepository.findById(2L), themeRepository.findById(2L).get()),
                new Reservation(null, "범블비", LocalDate.of(2025, 5, 15), reservationTimeRepository.findById(3L), themeRepository.findById(3L).get())
        );

        for (Reservation reservation : reservations) {
            reservationRepository.saveAndReturnId(reservation);
        }

        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
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
        ReservationRequest request = new ReservationRequest("루키", LocalDate.of(2025, 5, 3), 4L, 3L);

        // when
        ReservationResponse response = reservationService.add(request);

        // then
        ReservationTimeResponse expectedTimeResponse = new ReservationTimeResponse(4L, LocalTime.of(23, 53));
        ThemeResponse expectedThemeResponse = new ThemeResponse(3L, "레벨3 탈출", "우테코 레벨3를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");

        ReservationResponse expected = new ReservationResponse(4L, "루키", LocalDate.of(2025, 5, 3),expectedTimeResponse, expectedThemeResponse);
        assertThat(response).isEqualTo(expected);
    }

    @DisplayName("성공적으로 예약 정보를 삭제하면 예외가 발생하지 않는다")
    @Test
    void remove_test() {
        // given
        Long removeId = 3L;

        // when && then
        assertThatCode(() -> reservationService.remove(removeId))
                .doesNotThrowAnyException();
    }

    @DisplayName("지난 날짜인 경우 예외가 발생한다")
    @Test
    void past_day_exception_test() {
        // given
        ReservationRequest request = new ReservationRequest("루키", LocalDate.now().minusDays(1), 4L, 3L);

        // when & then
        assertThatThrownBy(() -> reservationService.add(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 날짜는 예약할 수 없습니다.");
    }

    @DisplayName("지난 시각인 경우 예외가 발생한다")
    @Test
    void past_time_exception_test() {
        // given
        ReservationRequest request = new ReservationRequest("루키", LocalDate.now(), 1L, 3L);

        // when & then
        assertThatThrownBy(() -> reservationService.add(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 시각은 예약할 수 없습니다.");
    }

    @DisplayName("미래 날짜인데 시간이 현재보다 과거인 경우 예외가 발생하지 않는다")
    @Test
    void future_test() {
        // given
        ReservationRequest request = new ReservationRequest("루키", LocalDate.now().plusDays(3), 1L, 1L);

        // when & then
        assertThatCode(() -> reservationService.add(request))
                .doesNotThrowAnyException();

    }

    @DisplayName("중복 예약을 하면 예외가 발생한다")
    @Test
    void reservation_duplicate_exception(){
        // given
        ReservationRequest request = new ReservationRequest("루키", LocalDate.of(2025, 5, 15), 3L, 2L);

        // when & then
        assertThatThrownBy(() -> reservationService.add(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 시간에 대한 예약이 존재합니다.");
    }

}
