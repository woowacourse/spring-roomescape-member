package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;
import roomescape.dto.theme.ThemeRequest;
import roomescape.dto.time.TimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql"})
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService timeService;

    @Autowired
    private ReservationThemeService themeService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        timeService.insertTime(new TimeRequest(LocalTime.parse("10:00")));
        themeService.insertTheme(new ThemeRequest("name", "desc", "thumb"));
        RestAssured.port = port;
    }

    @DisplayName("모든 예약을 조회한다.")
    @Test
    void getAllReservations() {
        // given
        reservationService.insertReservation(new ReservationRequest("user", LocalDate.parse("2025-01-01"), 1L, 1L));

        // when
        List<ReservationResponse> reservations = reservationService.getAllReservations();

        // then
        assertThat(reservations.size()).isEqualTo(1);
    }

    @DisplayName("예약을 추가한다.")
    @Test
    void insertReservation() {
        // given
        ReservationResponse response = reservationService.insertReservation(
                new ReservationRequest("user", LocalDate.parse("2025-01-01"), 1L, 1L));

        // when
        Long insertedReservationId = response.id();

        // then
        assertThat(insertedReservationId).isEqualTo(1L);
    }

    @DisplayName("동일한 날짜, 시간, 테마에 대한 예약은 불가능하다.")
    @Test
    void insertDuplicateReservation() {
        // given
        reservationService.insertReservation(new ReservationRequest("user", LocalDate.parse("2025-01-01"), 1L, 1L));

        // when
        ReservationRequest request = new ReservationRequest("user1", LocalDate.parse("2025-01-01"), 1L, 1L);

        // then
        assertThatThrownBy(() -> reservationService.insertReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 해당 시간에 예약이 존재합니다.");
    }

    @DisplayName("지난 날짜에 대한 예약은 불가능하다.")
    @Test
    void insertPastDate() {
        ReservationRequest request = new ReservationRequest("name", LocalDate.now().minusDays(1), 1L, 1L);

        assertThatThrownBy(() -> reservationService.insertReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 시간으로는 예약할 수 없습니다.");
    }

    @DisplayName("지난 시간에 대한 예약은 불가능하다.")
    @Test
    void insertPastTime() {
        // given
        timeService.insertTime(new TimeRequest(LocalTime.now().minusHours(1)));

        // when
        ReservationRequest request = new ReservationRequest("name", LocalDate.now(), 2L, 1L);

        // then
        assertThatThrownBy(
                () -> reservationService.insertReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지난 시간으로는 예약할 수 없습니다.");
    }

    @DisplayName("동일한 날짜, 시간, 테마에 대한 예약은 불가능하다.")
    @Test
    void insertSameReservation() {
        // given
        reservationService.insertReservation(new ReservationRequest("user", LocalDate.parse("2025-01-01"), 1L, 1L));

        // when
        ReservationRequest request = new ReservationRequest("user", LocalDate.parse("2025-01-01"), 1L, 1L);

        // then
        assertThatThrownBy(() -> reservationService.insertReservation(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 해당 시간에 예약이 존재합니다.");
    }

    @DisplayName("ID로 예약을 삭제한다.")
    @Test
    void deleteById() {
        // given
        reservationService.insertReservation(new ReservationRequest("user", LocalDate.parse("2025-01-01"), 1L, 1L));

        // when
        reservationService.deleteReservation(1L);

        // then
        assertThat(reservationService.getAllReservations()).hasSize(0);
    }
}
