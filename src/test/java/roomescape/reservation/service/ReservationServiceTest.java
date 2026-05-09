package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fixture.ReservationFixture;
import roomescape.fixture.ThemeFixture;
import roomescape.fixture.fake.FakeReservationRepository;
import roomescape.fixture.fake.FakeReservationTimeRepository;
import roomescape.fixture.fake.FakeThemeRepository;
import roomescape.reservation.application.dto.ReservationCreateCommand;
import roomescape.reservation.application.dto.ReservationQueryResult;
import roomescape.reservation.application.exception.ReservationException;
import roomescape.reservation.application.service.ReservationService;
import roomescape.reservation.domain.repository.ReservationRepository;
import roomescape.reservationtime.application.dto.ReservationTimeCreateCommand;
import roomescape.reservationtime.application.dto.ReservationTimeQueryResult;
import roomescape.reservationtime.application.service.ReservationTimeService;
import roomescape.theme.application.service.ThemeService;

class ReservationServiceTest {

    private ThemeService themeService;
    private ReservationTimeService timeService;
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        themeService = new ThemeService(new FakeThemeRepository());
        FakeReservationTimeRepository fakeTimeRepository = new FakeReservationTimeRepository();
        timeService = new ReservationTimeService(fakeTimeRepository, fakeTimeRepository);
        ReservationRepository reservationRepository = new FakeReservationRepository();
        reservationService = new ReservationService(reservationRepository, themeService, timeService);
    }

    @DisplayName("사용자의 방탈출 예약 시간 추가를 테스트합니다.")
    @Test
    void save_user_reservation_successfully() {
        setTimeAndTheme();

        ReservationCreateCommand request = ReservationFixture.starkCreateCommand(1L, 1L);
        ReservationQueryResult result = reservationService.save(request, LocalDateTime.of(2000, 1, 1, 0, 0));

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(result.id()).isEqualTo(1L);
            softly.assertThat(result.name()).isEqualTo(request.name());
            softly.assertThat(result.date()).isEqualTo(request.date());
            softly.assertThat(result.time()).isEqualTo(new ReservationTimeQueryResult(1L, LocalTime.of(10, 0)));
            softly.assertThat(result.theme()).isEqualTo(ThemeFixture.horrorThemeQueryResult(1L));
        });
    }

    @DisplayName("중복된 시간과 테마에 예약 추가 시 예외 발생을 테스트합니다.")
    @Test
    void validate_duplicated_reservation() {
        setTimeAndTheme();

        reservationService.save(ReservationFixture.starkCreateCommand(1L, 1L), LocalDateTime.of(2000, 1, 1, 0, 0));

        Assertions.assertThatThrownBy(
                        () -> reservationService.save(ReservationFixture.kayaCreateCommand(1L, 1L), LocalDateTime.of(2000, 1, 1, 0, 0)))
                .isInstanceOf(ReservationException.class)
                .hasMessage("이미 해당 날짜와 시간에 예약이 존재합니다.");
    }

    @DisplayName("오늘보다 이전 날짜 혹은 시간 예약 시도 시 예외 발생을 테스트합니다.")
    @Test
    void validate_throw_exception_when_reserving_past_date_or_time() {
        setTimeAndTheme();

        Assertions.assertThatThrownBy(
                        () -> reservationService.save(ReservationFixture.starkCreateCommand(1L, 1L), LocalDateTime.of(2026, 5, 6, 11, 0)))
                .isInstanceOf(ReservationException.class)
                .hasMessage("현재 시간보다 이전 시간으로 예약을 할 수 없습니다.");
    }

    @DisplayName("예약 삭제를 테스트합니다.")
    @Test
    void delete_reservation() {
        setTimeAndTheme();

        ReservationQueryResult result = reservationService.save(ReservationFixture.starkCreateCommand(1L, 1L), LocalDateTime.of(2026, 5, 6, 9, 0));

        assertThat(timeService.delete(result.id())).isEqualTo(1);
    }

    private void setTimeAndTheme() {
        themeService.save(ThemeFixture.horrorThemeCreateCommand());
        timeService.save(new ReservationTimeCreateCommand(LocalTime.of(10, 0)));
    }
}