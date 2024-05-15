package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import roomescape.BasicAcceptanceTest;
import roomescape.dto.request.AdminReservationRequest;

@Sql("/setReservation.sql")
class ReservationServiceTest extends BasicAcceptanceTest {
    @Autowired
    private ReservationService reservationService;

    @DisplayName("과거의 시간을 예약하면 예외를 발생시킨다")
    @Test
    void rejectPastTimeReservation() {
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(
                1L, LocalDate.now().minusDays(1), 1L, 1L
        );

        assertThatThrownBy(() -> reservationService.saveByAdmin(adminReservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 지난 시간입니다.");
    }

    @DisplayName("중복 예약할 시 예외를 발생시킨다")
    @Test
    void rejectDuplicateReservation() {
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(
                2L, LocalDate.now().plusDays(6), 2L, 2L
        );

        assertThatThrownBy(() -> reservationService.saveByAdmin(adminReservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 예약이 존재합니다.");
    }

    @DisplayName("가입되어 있지 않은 멤버로 예약할 시 예외를 발생시킨다")
    @Test
    void notExistMember() {
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(
                3L, LocalDate.now().plusDays(5), 1L, 1L
        );

        assertThatThrownBy(() -> reservationService.saveByAdmin(adminReservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("가입되어 있지 않은 유저입니다.");
    }

    @DisplayName("등록되지 않은 예약 시간으로 예약할 시 예외를 발생시킨다")
    @Test
    void notExistTime() {
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(
                1L, LocalDate.now().plusDays(5), 4L, 1L
        );

        assertThatThrownBy(() -> reservationService.saveByAdmin(adminReservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("예약할 수 없는 시간입니다.");
    }

    @DisplayName("등록되지 않은 테마로 예약할 시 예외를 발생시킨다")
    @Test
    void notExistTheme() {
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(
                1L, LocalDate.now().plusDays(5), 1L, 4L
        );

        assertThatThrownBy(() -> reservationService.saveByAdmin(adminReservationRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("예약할 수 없는 테마입니다.");
    }
}
