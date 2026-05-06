package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fake.FakeReservationRepository;
import roomescape.fake.FakeReservationTimeRepository;
import roomescape.fake.FakeThemeRepository;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.application.dto.ReservationQueryResult;
import roomescape.reservation.application.service.ReservationService;
import roomescape.reservation.application.exception.ReservationException;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservationtime.application.dto.ReservationTimeCreateCommand;
import roomescape.reservationtime.application.dto.ReservationTimeQueryResult;
import roomescape.reservationtime.application.service.ReservationTimeService;
import roomescape.theme.application.dto.ThemeCreateCommand;
import roomescape.theme.application.dto.ThemeQueryResult;
import roomescape.theme.application.service.ThemeService;

class ReservationServiceTest {

    private ThemeService themeService;
    private ReservationTimeService timeService;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        themeService = new ThemeService(new FakeThemeRepository());
        timeService = new ReservationTimeService(new FakeReservationTimeRepository());
        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationService = new ReservationService(reservationRepository, themeService, timeService);
    }

    @DisplayName("사용자의 방탈출 예약 시간 추가를 테스트합니다.")
    @Test
    void save_user_reservation_successfully() {
        themeService.save(new ThemeCreateCommand("theme name", "theme description", "theme img url"));
        timeService.save(new ReservationTimeCreateCommand(LocalTime.of(10, 0)));

        ReservationCreateCommand request = new ReservationCreateCommand("스타크", LocalDate.of(2026, 5, 6), 1L, 1L);
        ReservationQueryResult reservationQueryResult = reservationService.save(request, LocalDateTime.of(2000, 1, 1, 0, 0));

        SoftAssertions.assertSoftly(assertSoftly -> {
            assertSoftly.assertThat(reservationQueryResult.id()).isEqualTo(1L);
            assertSoftly.assertThat(reservationQueryResult.name()).isEqualTo("스타크");
            assertSoftly.assertThat(reservationQueryResult.date()).isEqualTo("2026-05-06");
            assertSoftly.assertThat(reservationQueryResult.time()).isEqualTo(new ReservationTimeQueryResult(1L, LocalTime.of(10,0)));
            assertSoftly.assertThat(reservationQueryResult.theme())
                    .isEqualTo(new ThemeQueryResult(1L, "theme name", "theme description", "theme img url"));
        });
    }

    @DisplayName("중복된 시간과 테마에 예약 추가 시 예외 발생을 테스트합니다.")
    @Test
    void validate_duplicated_reservation() {
        themeService.save(new ThemeCreateCommand("theme name", "theme description", "theme img url"));
        timeService.save(new ReservationTimeCreateCommand(LocalTime.of(10, 0)));

        ReservationCreateCommand firstRequest = new ReservationCreateCommand("스타크", LocalDate.of(2026, 5, 6), 1L, 1L);
        reservationService.save(firstRequest, LocalDateTime.of(2000, 1, 1, 0, 0));

        ReservationCreateCommand secondRequest = new ReservationCreateCommand("카야", LocalDate.of(2026, 5, 6), 1L, 1L);

        Assertions.assertThatThrownBy(() -> reservationService.save(secondRequest, LocalDateTime.of(2000, 1, 1, 0, 0)))
                .isInstanceOf(ReservationException.class)
                .hasMessage("[ERROR] 이미 해당 날짜와 시간에 예약이 존재합니다.");
    }

    @DisplayName("오늘보다 이전 날짜 혹은 시간 예약 시도 시 예외 발생을 테스트합니다.")
    @Test
    void validate_throw_exception_when_reserving_past_date_or_time() {
        themeService.save(new ThemeCreateCommand("theme name", "theme description", "theme img url"));
        timeService.save(new ReservationTimeCreateCommand(LocalTime.of(10, 0)));

        ReservationCreateCommand request = new ReservationCreateCommand("스타크", LocalDate.of(2026, 5, 6), 1L, 1L);

        Assertions.assertThatThrownBy(() -> reservationService.save(request, LocalDateTime.of(2026, 5, 6, 11, 0)))
                .isInstanceOf(ReservationException.class)
                .hasMessage("[ERROR] 현재 시간보다 이전 시간으로 예약을 할 수 없습니다.");
    }
}
