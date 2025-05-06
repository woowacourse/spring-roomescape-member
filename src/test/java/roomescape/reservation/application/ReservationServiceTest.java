package roomescape.reservation.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.GetThemeException;
import roomescape.global.exception.GetTimeException;
import roomescape.reservation.application.repository.ReservationRepository;
import roomescape.reservation.application.repository.ReservationTimeRepository;
import roomescape.reservation.application.repository.ThemeRepository;
import roomescape.reservation.application.service.ReservationService;
import roomescape.reservation.infrastructure.fake.FakeReservationDao;
import roomescape.reservation.infrastructure.fake.FakeReservationTimeDao;
import roomescape.reservation.infrastructure.fake.FakeThemeDao;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.reservation.presentation.dto.ReservationResponse;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ThemeRequest;

public class ReservationServiceTest {
    private static final ReservationRequest RESERVATION_REQUEST = new ReservationRequest(
            LocalDate.of(2025, 8, 5),
            "브라운",
            1L,
            1L
    );

    private ReservationService reservationService;
    private ReservationTimeRepository reservationTimeRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void init() {
        ReservationRepository reservationRepository = new FakeReservationDao();
        reservationTimeRepository = new FakeReservationTimeDao();
        themeRepository = new FakeThemeDao();
        reservationService = new ReservationService(reservationRepository, reservationTimeRepository, themeRepository);
    }

    @Test
    @DisplayName("예약 추가 테스트")
    void createReservationTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeRepository.insert(reservationTimeRequest.getStartAt());

        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themeRepository.insert(themeRequest);

        // when
        ReservationResponse reservationResponse = reservationService.createReservation(RESERVATION_REQUEST);

        // then
        assertThat(reservationResponse.getId()).isEqualTo(1L);
        assertThat(reservationResponse.getDate()).isEqualTo(LocalDate.of(2025, 8, 5));
        assertThat(reservationResponse.getName()).isEqualTo("브라운");
        assertThat(reservationResponse.getTime().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("예약 추가 시 예약 시간이 조회되지 않으면 예외가 발생한다")
    void createReservationExceptionTest() {
        // given
        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themeRepository.insert(themeRequest);

        // when - then
        assertThatThrownBy(() -> reservationService.createReservation(RESERVATION_REQUEST))
                .isInstanceOf(GetTimeException.class)
                .hasMessage("[ERROR] 예약 시간 정보를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("예약 추가 시 테마가 조회되지 않으면 예외가 발생한다")
    void createThemeExceptionTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeRepository.insert(reservationTimeRequest.getStartAt());

        // when - then
        assertThatThrownBy(() -> reservationService.createReservation(RESERVATION_REQUEST))
                .isInstanceOf(GetThemeException.class)
                .hasMessage("[ERROR] 테마 정보를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("예약 전체 조회 테스트")
    void getReservationsTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeRepository.insert(reservationTimeRequest.getStartAt());

        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themeRepository.insert(themeRequest);

        reservationService.createReservation(RESERVATION_REQUEST);

        // when - then
        assertThat(reservationService.getReservations().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 조회 테스트")
    void getReservationTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeRepository.insert(reservationTimeRequest.getStartAt());

        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themeRepository.insert(themeRequest);

        reservationService.createReservation(RESERVATION_REQUEST);

        // when - then
        assertThat(reservationService.getReservations().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 삭제 테스트")
    void deleteReservationTest() {
        // given
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(15, 40));
        reservationTimeRepository.insert(reservationTimeRequest.getStartAt());

        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        themeRepository.insert(themeRequest);

        reservationService.createReservation(RESERVATION_REQUEST);

        // when
        reservationService.deleteReservation(1L);

        // then
        assertThat(reservationService.getReservations().size()).isEqualTo(0);
    }
}
