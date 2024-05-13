package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.Reservation;
import roomescape.service.LoginService;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationResponse;

@WebMvcTest(ReservationApiController.class)
class ReservationApiControllerTest {

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private LoginService loginService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final Reservation reservation1 = new Reservation(
            1L,
            1L, "재즈", "admin", "email", "password",
            1L, "테마이름", "테마내용", "테마썸네일",
            "2024-04-22",
            2L, "17:30");
    private final Reservation reservation2 = new Reservation(
            2L,
            2L, "안돌", "user", "email", "password",
            1L, "테마이름", "테마내용", "테마썸네일",
            "2023-09-08",
            1L, "15:30");

    @DisplayName("/reservations GET 요청 시 모든 예약 목록과 200 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_and_saved_all_reservations_when_get_request() throws Exception {
        List<ReservationResponse> responseDtos = List.of(
                new ReservationResponse(reservation1),
                new ReservationResponse(reservation2)
        );

        given(reservationService.findAllReservations()).willReturn(responseDtos);

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @DisplayName("/reservations/{id} DELETE 요청 시 204 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_when_delete_request() throws Exception {
        mockMvc.perform(delete("/reservations/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
