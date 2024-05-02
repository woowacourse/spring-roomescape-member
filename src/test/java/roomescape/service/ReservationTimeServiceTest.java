package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ReservationTimeBookedRequest;
import roomescape.dto.ReservationTimeBookedResponse;
import roomescape.dto.ReservationTimeResponse;
import roomescape.dto.ReservationTimeSaveRequest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = {"/test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @DisplayName("예약 시간 목록 조회")
    @Test
    void getTimes() {
        final List<ReservationTimeResponse> reservationTimeResponses = reservationTimeService.getTimes();
        assertThat(reservationTimeResponses.size()).isEqualTo(7);
    }

    @DisplayName("중복된 시간 저장")
    @Test
    void saveExistTime() {
        assertThatCode(() ->
                reservationTimeService.saveTime(new ReservationTimeSaveRequest(LocalTime.parse("09:00")))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약 시간 추가")
    @Test
    void saveTime() {
        final ReservationTimeSaveRequest reservationTimeSaveRequest = new ReservationTimeSaveRequest(LocalTime.parse("01:10"));
        final ReservationTimeResponse reservationTimeResponse = reservationTimeService.saveTime(reservationTimeSaveRequest);
        assertThat(reservationTimeResponse).isEqualTo(new ReservationTimeResponse(8L, LocalTime.parse("01:10")));
    }

    @DisplayName("예약 시간 삭제")
    @Test
    void deleteTime() {
        reservationTimeService.deleteTime(5L);
        assertThat(reservationTimeService.getTimes().size()).isEqualTo(6);
    }

    @DisplayName("존재하지 않는 예약 시간 삭제")
    @Test
    void deleteTimeNotFound() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(8L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("예약이 존재하는 시간 삭제")
    @Test
    void deleteTimeExistReservation() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 날짜의 테마에 대한 전체 시간 예약 여부 조회")
    @Test
    void getTimesWithBooked() {
        final List<ReservationTimeBookedResponse> reservationTimeBookedResponses = reservationTimeService.getTimesWithBooked(
                new ReservationTimeBookedRequest(LocalDate.parse("2024-04-29"), 1L));
        assertThat(reservationTimeBookedResponses)
                .hasSize(7)
                .containsExactly(
                        new ReservationTimeBookedResponse(1L, LocalTime.parse("09:00"), true),
                        new ReservationTimeBookedResponse(2L, LocalTime.parse("10:00"), false),
                        new ReservationTimeBookedResponse(3L, LocalTime.parse("11:00"), false),
                        new ReservationTimeBookedResponse(4L, LocalTime.parse("12:00"), false),
                        new ReservationTimeBookedResponse(5L, LocalTime.parse("13:00"), false),
                        new ReservationTimeBookedResponse(6L, LocalTime.parse("14:00"), false),
                        new ReservationTimeBookedResponse(7L, LocalTime.parse("15:00"), false)
                );
    }
}

