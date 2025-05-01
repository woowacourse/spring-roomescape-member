package roomescape.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.application.repository.ReservationTimeRepository;
import roomescape.reservation.application.repository.ThemeRepository;
import roomescape.reservation.application.service.ReservationService;
import roomescape.reservation.application.service.ReservationTimeService;
import roomescape.reservation.application.service.ThemeService;
import roomescape.reservation.infrastructure.fake.FakeReservationDao;
import roomescape.reservation.infrastructure.fake.FakeReservationTimeDao;
import roomescape.reservation.infrastructure.fake.FakeThemeDao;
import roomescape.reservation.presentation.dto.AvailableReservationTimeResponse;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ReservationTimeResponse;
import roomescape.reservation.presentation.dto.ThemeRequest;

public class ReservationTimeServiceTest {
    private ReservationTimeService reservationTimeService;
    private ReservationService reservationService;
    private ThemeService themeService;

    @BeforeEach
    void init() {
        ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeDao();
        ReservationRepository reservationRepository = new FakeReservationDao();
        ThemeRepository themeRepository = new FakeThemeDao();
        reservationTimeService = new ReservationTimeService(reservationTimeRepository, reservationRepository);
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
        themeService = new ThemeService(reservationRepository, themeRepository);
    }

    @Test
    @DisplayName("예약 시간 추가 테스트")
    void createReservationTimeTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));

        // when
        ReservationTimeResponse reservationTime = reservationTimeService.createReservationTime(reservationTimeRequest);

        // then
        assertThat(reservationTime.getId()).isEqualTo(1L);
        assertThat(reservationTime.getStartAt()).isEqualTo(LocalTime.of(15, 40));
    }

    @Test
    @DisplayName("예약 시간 전체 조회 테스트")
    void getReservationTimesTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeService.createReservationTime(reservationTimeRequest);

        // when - then
        assertThat(reservationTimeService.getReservationTimes().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 가능 시간 조회 테스트")
    void getAvailableReservationTimesTest() {
        // given
        ReservationTimeRequest availableReservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        ReservationTimeRequest unAvailableReservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 50));
        reservationTimeService.createReservationTime(availableReservationTimeRequest);
        reservationTimeService.createReservationTime(unAvailableReservationTimeRequest);

        ThemeRequest themeRequest = new ThemeRequest(
                "레벨2 탈출",
                "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );
        themeService.createTheme(themeRequest);

        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.of(2025, 8, 5),
                "브라운",
                1L,
                1L
        );
        reservationService.createReservation(reservationRequest);

        // when
        List<AvailableReservationTimeResponse> reservationTimes = reservationTimeService.getReservationTimes(
                LocalDate.of(2025, 8, 5), 1L);

        // then
        assertThat(reservationTimes.stream()
                .filter(AvailableReservationTimeResponse::isAlreadyBooked)
                .count())
                .isEqualTo(1);
    }

    @Test
    @DisplayName("예약 시간 삭제 테스트")
    void deleteReservationTimeTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeService.createReservationTime(reservationTimeRequest);

        // when
        reservationTimeService.deleteReservationTime(1L);

        // then
        assertThat(reservationTimeService.getReservationTimes().size()).isEqualTo(0);

    }
}
