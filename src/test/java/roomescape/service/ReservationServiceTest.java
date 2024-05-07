package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import roomescape.handler.exception.CustomException;
import roomescape.handler.exception.ExceptionCode;
import roomescape.service.dto.request.ReservationRequest;
import roomescape.service.dto.request.ReservationTimeRequest;
import roomescape.service.dto.request.ThemeRequest;
import roomescape.service.dto.response.ReservationResponse;
import roomescape.service.dto.response.ReservationTimeResponse;
import roomescape.service.dto.response.ThemeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Transactional
class ReservationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ThemeService themeService;

    @DisplayName("예약 생성 테스트")
    @Test
    void createReservation() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTimeResponse reservationTime = reservationTimeService.createReservationTime(reservationTimeRequest);

        ThemeRequest themeRequest = new ThemeRequest("happy", "hi", "abcd.html");
        ThemeResponse theme = themeService.createTheme(themeRequest);

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), reservationTime.id(), theme.id());
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest);

        assertAll(
                () -> assertThat(reservationResponse.name()).isEqualTo("브라운"),
                () -> assertThat(reservationResponse.date()).isEqualTo(LocalDate.of(2999, 8, 5))
        );
    }

    @DisplayName("예약시간이 없는 경우 예외가 발생한다.")
    @Test
    void reservationTimeIsNotExist() {
        ThemeRequest themeRequest = new ThemeRequest("happy", "hi", "abcd.html");
        ThemeResponse theme = themeService.createTheme(themeRequest);

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L, theme.id());
        assertThatThrownBy(() -> reservationService.createReservation(reservationRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.NOT_FOUND_RESERVATION_TIME.getErrorMessage());
    }

    @DisplayName("과거 시간을 예약하는 경우 예외가 발생한다.")
    @Test
    void validatePastTime() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTimeResponse reservationTime = reservationTimeService.createReservationTime(reservationTimeRequest);

        ThemeRequest themeRequest = new ThemeRequest("happy", "hi", "abcd.html");
        ThemeResponse theme = themeService.createTheme(themeRequest);

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(1999, 8, 5), reservationTime.id(), theme.id());
        assertThatThrownBy(() -> reservationService.createReservation(reservationRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(ExceptionCode.PAST_TIME_SLOT_RESERVATION.getErrorMessage());
    }


    @DisplayName("모든 예약 조회 테스트")
    @Test
    void findAllReservations() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTimeResponse reservationTime = reservationTimeService.createReservationTime(reservationTimeRequest);

        ThemeRequest themeRequest = new ThemeRequest("happy", "hi", "abcd.html");
        ThemeResponse theme = themeService.createTheme(themeRequest);

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), reservationTime.id(), theme.id());
        reservationService.createReservation(reservationRequest);

        List<ReservationResponse> reservations = reservationService.findAllReservations();

        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservations.get(0).name()).isEqualTo("브라운"),
                () -> assertThat(reservations.get(0).date()).isEqualTo(LocalDate.of(2999, 8, 5))
        );
    }

    @DisplayName("예약 삭제 테스트")
    @Test
    void deleteReservation() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTimeResponse reservationTime = reservationTimeService.createReservationTime(reservationTimeRequest);

        ThemeRequest themeRequest = new ThemeRequest("happy", "hi", "abcd.html");
        ThemeResponse theme = themeService.createTheme(themeRequest);

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), reservationTime.id(), theme.id());
        ReservationResponse savedReservation = reservationService.createReservation(reservationRequest);

        reservationService.deleteReservation(savedReservation.id());

        List<ReservationResponse> reservations = reservationService.findAllReservations();

        assertThat(reservations).isEmpty();
    }
}
