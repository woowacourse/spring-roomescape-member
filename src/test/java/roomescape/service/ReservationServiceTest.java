package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.exception.InvalidReservationException;
import roomescape.service.dto.AdminReservationRequest;
import roomescape.service.dto.ReservationResponse;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Sql(scripts = "/test_data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationServiceTest {
    @Autowired
    private ReservationService reservationService;

    @DisplayName("새로운 예약을 저장한다.")
    @Test
    void create() {
        //given
        String name = "lini";
        String date = "2024-10-04";
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(date, 1, 1, 1);

        //when
        ReservationResponse result = reservationService.create(adminReservationRequest);

        //then
        assertThat(result.id()).isNotZero();
    }

    @DisplayName("모든 예약 내역을 조회한다.")
    @Test
    void findAll() {
        //when
        List<ReservationResponse> reservations = reservationService.findAll();

        //then
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("id로 예약을 삭제한다.")
    @Test
    void deleteById() {
        //when
        reservationService.deleteById(1);

        //then
        assertThat(reservationService.findAll()).hasSize(0);
    }

    @DisplayName("해당 테마와 일정으로 예약이 존재하면 예외를 발생시킨다.")
    @Test
    void duplicatedReservation() {
        //given
        String date = "2024-06-04";
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(date, 1, 1, 1);

        //when & then
        assertThatThrownBy(() -> reservationService.create(adminReservationRequest))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("존재하지 않는 시간으로 예약을 추가하면 예외를 발생시킨다.")
    @Test
    void cannotCreateByUnknownTime() {
        //given
        String name = "lini";
        String date = "2024-10-04";
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(date, 1, 0, 1);

        //when & then
        assertThatThrownBy(() -> reservationService.create(adminReservationRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("더이상 존재하지 않는 시간입니다.");
    }

    @DisplayName("존재하지 않는 테마로 예약을 추가하면 예외를 발생시킨다.")
    @Test
    void cannotCreateByUnknownTheme() {
        //given
        String name = "lini";
        String date = "2024-10-04";
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(date, 0, 1, 1);

        //when & then
        assertThatThrownBy(() -> reservationService.create(adminReservationRequest))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("더이상 존재하지 않는 테마입니다.");
    }
}
