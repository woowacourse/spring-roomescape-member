package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.fake.FakeThemeRepository;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.exception.ReservationException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.dto.ReservationTimeCreateRequest;
import roomescape.reservationtime.dto.ReservationTimeResponse;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.dto.ThemeCreateRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

class ReservationServiceTest {

    private ThemeService themeService = new ThemeService(new FakeThemeRepository());
    private ReservationTimeService timeService = new ReservationTimeService(new FakeReservationTimeRepository());

    private ReservationRepository reservationRepository = new FakeReservationRepository();
    private ReservationService reservationService = new ReservationService(reservationRepository, themeService,
            timeService);

    @DisplayName("사용자의 방탈출 예약 시간 추가를 테스트합니다.")
    @Test
    void save_user_reservation_successfully() {
        themeService.saveTheme(new ThemeCreateRequest("theme name", "theme description", "theme img url"));
        timeService.saveReservationTime(new ReservationTimeCreateRequest(LocalTime.of(10, 0)));

        ReservationCreateRequest request = new ReservationCreateRequest("스타크", LocalDate.of(2026, 5, 6), 1L, 1L);
        ReservationResponse reservationResponse = reservationService.saveReservation(request);

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(reservationResponse.id()).isEqualTo(1L);
            assertSoftly.assertThat(reservationResponse.name()).isEqualTo("스타크");
            assertSoftly.assertThat(reservationResponse.date()).isEqualTo("2026-05-06");
            assertSoftly.assertThat(reservationResponse.time()).isEqualTo(new ReservationTimeResponse(1L, "10:00"));
            assertSoftly.assertThat(reservationResponse.theme())
                    .isEqualTo(new ThemeResponse(1L, "theme name", "theme description", "theme img url"));
        });
    }

    @DisplayName("중복된 시간과 테마에 예약 추가 시 예외 발생을 테스트합니다.")
    @Test
    void validate_duplicated_reservation() {
        themeService.saveTheme(new ThemeCreateRequest("theme name", "theme description", "theme img url"));
        timeService.saveReservationTime(new ReservationTimeCreateRequest(LocalTime.of(10, 0)));

        ReservationCreateRequest firstRequest = new ReservationCreateRequest("스타크", LocalDate.of(2026, 5, 6), 1L, 1L);
        reservationService.saveReservation(firstRequest);

        ReservationCreateRequest secondRequest = new ReservationCreateRequest("카야", LocalDate.of(2026, 5, 6), 1L, 1L);

        Assertions.assertThatThrownBy(() -> reservationService.saveReservation(secondRequest))
                .isInstanceOf(ReservationException.class)
                .hasMessage("[ERROR] 이미 해당 날짜와 시간에 예약이 존재합니다.");
    }
}
