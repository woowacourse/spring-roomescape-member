package roomescape.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.fixture.DateTimeFixture.DAY_AFTER_TOMORROW;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.dto.request.ReservationAddRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.service.ReservationService;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    @DisplayName("전체 예약 목록을 읽는 요청을 처리할 수 있다")
    @Test
    void should_response_all_reservations_when_requested() throws Exception {
        when(reservationService.findAllReservation()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @DisplayName("예약 추가 요청을 처리할 수 있다")
    @Test
    void should_add_reservation_when_post_request_reservations() throws Exception {
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest("썬", DAY_AFTER_TOMORROW, 1L, 1L);
        ReservationResponse mockResponse = new ReservationResponse(1L, "썬", DAY_AFTER_TOMORROW, 1L, 1L);

        when(reservationService.saveReservation(reservationAddRequest)).thenReturn(mockResponse);

        mockMvc.perform(post("/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservationAddRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/reservations/" + mockResponse.id()));
    }

    @DisplayName("예약 삭제 요청을 처리할 수 있다")
    @Test
    void should_remove_reservation_when_delete_request_reservations_id() throws Exception {
        mockMvc.perform(delete("/reservations/{id}", 1))
                .andExpect(status().isNoContent());
    }
}
