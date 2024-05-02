package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Name;
import roomescape.dto.request.ReservationAddRequest;
import roomescape.dto.response.ReservationResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("예약을 추가하고 id값을 붙여서 응답 DTO를 생성한다.")
    void addReservation() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(new Name("네오"),
                LocalDate.now().plusDays(1), 1L, 1L);

        ReservationResponse reservationResponse = reservationService.addReservation(reservationAddRequest);

        assertThat(reservationResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("존재하지 않는 time_id로 예약을 추가하면 예외를 발생시킨다.")
    void addReservationInvalidGetTimeGetId() {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(new Name("네오"),
                LocalDate.now().plusDays(1), -1L, 1L);

        assertThatThrownBy(() -> reservationService.addReservation(reservationAddRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("id에 맞는 예약을 삭제한다.")
    void deleteReservation() {
        reservationService.deleteReservation(1L);

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM reservation", Integer.class);
        assertThat(count).isOne();
    }

    @Test
    @DisplayName("모든 예약들을 조회한다.")
    void getReservations() {
        List<ReservationResponse> reservations = reservationService.findReservations();

        assertThat(reservations).hasSize(2);
    }
}
