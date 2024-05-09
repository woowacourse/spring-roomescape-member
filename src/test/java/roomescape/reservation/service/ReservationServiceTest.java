package roomescape.reservation.service;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.handler.exception.CustomException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        Reservation reservation = reservationService.createReservation(reservationRequest);

        assertAll(
                () -> assertThat(reservation.getName()).isEqualTo("브라운"),
                () -> assertThat(reservation.getDate()).isEqualTo(LocalDate.of(2999, 8, 5))
        );
    }

    @DisplayName("모든 예약 조회 테스트")
    @Test
    void findAllReservations() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES(?, ?, ?)", "happy", "hi", "abcd.html");

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L, 1L);
        reservationService.createReservation(reservationRequest);

        List<Reservation> reservations = reservationService.findAllReservations();

        assertAll(
                () -> assertThat(reservations).hasSize(1),
                () -> assertThat(reservations.get(0).getName()).isEqualTo("브라운"),
                () -> assertThat(reservations.get(0).getDate()).isEqualTo(LocalDate.of(2999, 8, 5))
        );
    }

    @DisplayName("예약 삭제 테스트")
    @Test
    void deleteReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES(?)", LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES(?, ?, ?)", "happy", "hi", "abcd.html");

        ReservationRequest reservationRequest = new ReservationRequest("브라운", LocalDate.of(2999, 8, 5), 1L, 1L);
        Reservation savedReservation = reservationService.createReservation(reservationRequest);

        reservationService.deleteReservation(savedReservation.getId());

        List<Reservation> reservations = reservationService.findAllReservations();

        assertThat(reservations).isEmpty();
    }
}
