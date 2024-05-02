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

    private final Reservation reservation1 = new Reservation(
            1L, "재즈",
            1L, "테마이름", "테마내용", "테마썸네일",
            "2024-04-22",
            2L, "17:30");
    private final Reservation reservation2 = new Reservation(
            2L, "안돌",
            1L, "테마이름", "테마내용", "테마썸네일",
            "2023-09-08",
            1L, "15:30");

    @DisplayName("/reservations GET 요청 시 모든 예약 목록과 200 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_and_saved_all_reservations_when_get_request() throws Exception {
        List<ReservationResponseDto> responseDtos = List.of(
                new ReservationResponseDto(reservation1),
                new ReservationResponseDto(reservation2)
        );

        given(reservationService.findAllReservations()).willReturn(responseDtos);

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @DisplayName("/reservations POST 요청 시 저장된 예약과 201 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_and_saved_reservation_when_post_request() throws Exception {
        ReservationRequestDto requestDto = new ReservationRequestDto("재즈", 1L, "2024-04-22", 2L);
        String requestBody = objectMapper.writeValueAsString(requestDto);
        ReservationResponseDto responseDto = new ReservationResponseDto(reservation1);

        given(reservationService.createReservation(any())).willReturn(responseDto);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("재즈")))
                .andExpect(jsonPath("$.theme.id", is(1)))
                .andExpect(jsonPath("$.theme.name", is("테마이름")))
                .andExpect(jsonPath("$.theme.description", is("테마내용")))
                .andExpect(jsonPath("$.theme.thumbnail", is("테마썸네일")))
                .andExpect(jsonPath("$.date", is("2024-04-22")))
                .andExpect(jsonPath("$.time.id", is(2)))
                .andExpect(jsonPath("$.time.startAt", is("17:30")));
    }

    @DisplayName("/reservations/{id} DELETE 요청 시 204 상태 코드를 응답한다.")
    @Test
    void return_200_status_code_when_delete_request() throws Exception {
        mockMvc.perform(delete("/reservations/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
