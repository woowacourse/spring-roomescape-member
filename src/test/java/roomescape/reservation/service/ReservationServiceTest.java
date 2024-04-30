package roomescape.reservation.service;

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
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.request.ReservationTimeRequest;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.handler.exception.CustomException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationService reservationService;

    @DisplayName("예약시간이 없는 경우 예외가 발생한다.")
    @Test
    void reservationTimeIsNotExist() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES(?, ?, ?)", "happy", "hi", "abcd.html");

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L,1L);

        assertThatThrownBy(() -> reservationService.createReservation(reservationRequest))
                .isInstanceOf(CustomException.class);
    }

    @DisplayName("예약 생성 테스트")
    @Test
    void createReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES(?, ?, ?)", "happy", "hi", "abcd.html");

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L, 1L);
        ReservationResponse reservationResponse = reservationService.createReservation(reservationRequest);

        assertAll(
                () -> assertThat(reservationResponse.name()).isEqualTo("브라운"),
                () -> assertThat(reservationResponse.date()).isEqualTo(LocalDate.of(2999, 8, 5))
        );
    }

    @DisplayName("모든 예약 조회 테스트")
    @Test
    void findAllReservations() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES(?, ?, ?)", "happy", "hi", "abcd.html");

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L, 1L);
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
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES(?, ?, ?)", "happy", "hi", "abcd.html");

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L, 1L);
        ReservationResponse savedReservation = reservationService.createReservation(reservationRequest);

        reservationService.deleteReservation(savedReservation.id());

        List<ReservationResponse> reservations = reservationService.findAllReservations();

        assertThat(reservations).isEmpty();
    }
}
