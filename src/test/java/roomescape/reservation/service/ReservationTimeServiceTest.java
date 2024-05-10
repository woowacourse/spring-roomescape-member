package roomescape.reservation.service;

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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.reservation.dto.reservation.ReservationRequest;
import roomescape.reservation.dto.theme.ThemeRequest;
import roomescape.reservation.dto.time.BookableTimeResponse;
import roomescape.reservation.dto.time.TimeRequest;
import roomescape.reservation.dto.time.TimeResponse;
import roomescape.reservation.service.ReservationService;
import roomescape.reservation.service.ReservationThemeService;
import roomescape.reservation.service.ReservationTimeService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/test_schema.sql"})
class ReservationTimeServiceTest {

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
        RestAssured.port = port;
    }

    @DisplayName("시간을 추가한다.")
    @Test
    void insertTime() {
        // given
        TimeResponse response = timeService.insertTime(new TimeRequest(LocalTime.now().plusHours(1)));

        // when
        Long insertedTimeId = response.id();

        // then
        assertThat(insertedTimeId).isEqualTo(1L);
    }

    @DisplayName("동일한 시간은 추가할 수 없다.")
    @Test
    void insertSameTime() {
        // given
        timeService.insertTime(new TimeRequest(LocalTime.parse("10:00")));

        // when
        TimeRequest request = new TimeRequest(LocalTime.parse("10:00"));

        // then
        assertThatThrownBy(() -> timeService.insertTime(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 시간이 존재합니다.");
    }

    @DisplayName("모든 시간을 조회한다.")
    @Test
    void getAllReservationTimes() {
        // given
        timeService.insertTime(new TimeRequest(LocalTime.parse("10:00")));
        timeService.insertTime(new TimeRequest(LocalTime.parse("11:00")));

        // when
        List<TimeResponse> times = timeService.getAllTimes();

        // then
        assertThat(times).hasSize(2);
    }

    @DisplayName("모든 예약 가능한 시간을 조회한다.")
    @Test
    void getAllBookableTimes() {
        // given
        timeService.insertTime(new TimeRequest(LocalTime.parse("10:00")));
        timeService.insertTime(new TimeRequest(LocalTime.parse("11:00")));
        themeService.insertTheme(new ThemeRequest("name", "desc", "thumb"));
        reservationService.insertReservation(new ReservationRequest("user", LocalDate.now().plusDays(1), 1L, 1L));

        // when
        List<BookableTimeResponse> bookableTimes = timeService.getAllBookableTimes(
                LocalDate.now().plusDays(1).toString(), 1L);

        // then
        assertThat(bookableTimes).hasSize(2);
        assertThat(bookableTimes).extracting("booked").containsExactly(true, false);
    }

    @DisplayName("테마가 존재하지 않으면 예약 가능한 시간을 조회할 수 없다.")
    @Test
    void getAllBookableTimes_WithNotExistTheme() {
        assertThatThrownBy(() -> timeService.getAllBookableTimes(LocalDate.now().toString(), 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("테마가 존재하지 않아 예약 가능 시간을 조회할 수 없습니다.");
    }

    @DisplayName("ID로 시간을 삭제한다.")
    @Test
    void deleteTime() {
        // given
        timeService.insertTime(new TimeRequest(LocalTime.parse("10:00")));

        // when
        timeService.deleteTime(1L);

        // then
        assertThat(timeService.getAllTimes()).hasSize(0);
    }

    @DisplayName("예약이 존재하는 시간은 삭제할 수 없다.")
    @Test
    void deleteReservedTime() {
        // given
        timeService.insertTime(new TimeRequest(LocalTime.parse("10:00")));
        themeService.insertTheme(new ThemeRequest("name", "desc", "thumb"));
        reservationService.insertReservation(new ReservationRequest("user", LocalDate.now().plusDays(1), 1L, 1L));

        // when & then
        assertThatThrownBy(() -> timeService.deleteTime(1L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("예약이 존재하는 시간은 삭제할 수 없습니다.");

    }
}
