package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialDataFixture.INITIAL_RESERVATION_COUNT;
import static roomescape.InitialDataFixture.RESERVATION_1;
import static roomescape.InitialDataFixture.RESERVATION_2;
import static roomescape.InitialDataFixture.THEME_2;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exceptions.ValidationException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"/schema.sql", "/initial_test_data.sql"})
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("과거 시간을 예약하려는 경우 예외를 발생시킨다.")
    void savePastGetTime() {
        ReservationRequest reservationRequest = new ReservationRequest(
                "네오",
                RESERVATION_1.getDate(),
                RESERVATION_1.getTime().getId(),
                THEME_2.getId()
        );

        assertThatThrownBy(() -> reservationService.addReservation(reservationRequest))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("같은 날짜, 시간, 테마에 예약을 하는 경우 예외를 발생시킨다.")
    void saveSameReservation() {
        ReservationRequest reservationRequest = new ReservationRequest(
                "네오",
                RESERVATION_2.getDate(),
                RESERVATION_2.getTime().getId(),
                RESERVATION_2.getTheme().getId()
        );

        assertThatThrownBy(() -> reservationService.addReservation(reservationRequest))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("예약을 추가하고 id값을 붙여서 응답 DTO를 생성한다.")
    void addReservation() {
        ReservationRequest reservationRequest = new ReservationRequest(
                "네오",
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        ReservationResponse reservationResponse = reservationService.addReservation(reservationRequest);

        assertThat(reservationResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 time_id로 예약을 추가하면 예외를 발생시킨다.")
    void addReservationInvalidGetTimeGetId() {
        ReservationRequest reservationRequest = new ReservationRequest(
                "네오",
                LocalDate.now().plusDays(1),
                -1L,
                1L
        );

        assertThatThrownBy(() -> reservationService.addReservation(reservationRequest))
                .isInstanceOf(ValidationException.class);
    }

    @Test
    @DisplayName("id에 맞는 예약을 삭제한다.")
    void deleteReservation() {
        reservationService.deleteReservation(RESERVATION_1.getId());

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
        assertThat(count).isEqualTo(INITIAL_RESERVATION_COUNT - 1);
    }

    @Test
    @DisplayName("모든 예약들을 조회한다.")
    void getReservations() {
        List<ReservationResponse> reservations = reservationService.findReservations();

        assertThat(reservations).hasSize(INITIAL_RESERVATION_COUNT);
    }
}
