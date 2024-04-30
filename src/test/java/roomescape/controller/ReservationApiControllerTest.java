package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.Reservation;
import roomescape.service.ReservationService;
import roomescape.service.dto.ReservationRequestDto;
import roomescape.service.dto.ReservationResponseDto;

@WebMvcTest(ReservationApiController.class)
class ReservationApiControllerTest {

    @MockBean
    private ReservationService reservationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("/reservations GET 요청 시 모든 예약 목록과 200 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_and_saved_all_reservations_when_get_request() throws Exception {
        List<ReservationResponseDto> responseDtos = List.of(
                new ReservationResponseDto(new Reservation(1L, "안돌", "2023-09-08", 1L, "15:30")),
                new ReservationResponseDto(new Reservation(1L, "재즈", "2023-11-30", 2L, "17:30"))
        );

        given(reservationService.findAllReservations()).willReturn(responseDtos);

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @DisplayName("/reservations POST 요청 시 저장된 예약과 200 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_and_saved_reservation_when_post_request() throws Exception {
        ReservationRequestDto requestDto = new ReservationRequestDto("재즈", "2024-04-22", 1L);
        String requestBody = objectMapper.writeValueAsString(requestDto);

        ReservationResponseDto responseDto = new ReservationResponseDto(
                new Reservation(1L, "재즈", "2024-04-22", 1L, "15:30")
        );

        given(reservationService.createReservation(any())).willReturn(responseDto);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("재즈")))
                .andExpect(jsonPath("$.date", is("2024-04-22")))
                .andExpect(jsonPath("$.time.id", is(1)))
                .andExpect(jsonPath("$.time.startAt", is("15:30")));
    }

    @DisplayName("/reservations/{id} DELETE 요청 시 200 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_when_delete_request() throws Exception {
        mockMvc.perform(delete("/reservations/{id}", 1))
                .andExpect(status().isOk());
    }
}
