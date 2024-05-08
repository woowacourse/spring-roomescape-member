package roomescape.presentation.reservation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import roomescape.application.member.TokenManager;
import roomescape.application.reservation.ReservationService;
import roomescape.application.reservation.dto.response.ReservationResponse;
import roomescape.application.reservation.dto.response.ReservationTimeResponse;
import roomescape.application.reservation.dto.response.ThemeResponse;
import roomescape.presentation.ControllerTest;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest extends ControllerTest {

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private TokenManager tokenManager;

    @DisplayName("저장된 모든 예약을 반환한다.")
    @Test
    void shouldReturnReservationResponsesWhenReservationsExist() throws Exception {
        ReservationResponse reservationResponse = new ReservationResponse(
                1L, "test", LocalDate.of(2024, 12, 25),
                new ReservationTimeResponse(1L, LocalTime.now()),
                new ThemeResponse(1L, "test", "test", "test"));
        String reservationResponsesJson = objectMapper.writeValueAsString(List.of(reservationResponse));

        given(reservationService.findAll())
                .willReturn(List.of(reservationResponse));

        mvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().json(reservationResponsesJson));
    }

    @DisplayName("저장된 예약이 없다면 빈 리스트를 반환한다.")
    @Test
    void shouldReturnEmptyListWhenReservationsIsEmpty() throws Exception {
        String reservationResponsesJson = objectMapper.writeValueAsString(List.of());

        given(reservationService.findAll())
                .willReturn(List.of());

        mvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().json(reservationResponsesJson));
    }

    @DisplayName("존재하는 예약의 id로 삭제 요청을 하면 204 No Content 응답을 반환한다.")
    @Test
    void shouldReturn204SuccessWhenReservationIdExist() throws Exception {
        mvc.perform(delete("/reservations/1"))
                .andExpect(status().isNoContent());

        then(reservationService).should(times(1)).deleteById(any(Long.class));
    }

    @DisplayName("존재하지 않는 예약의 id로 삭제 요청을 하면 400 Bad Request 응답을 반환한다.")
    @Test
    void shouldReturn400BadRequestWhenReservationIdNotExist() throws Exception {
        doThrow(new IllegalArgumentException("존재하지 않는 예약입니다."))
                .when(reservationService).deleteById(1L);

        mvc.perform(delete("/reservations/1"))
                .andExpect(status().isBadRequest());

        then(reservationService).should(times(1)).deleteById(any(Long.class));
    }
}
