package roomescape.presentation.controller;//package roomescape.presentation.controller;
//
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doNothing;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.time.LocalDate;
//import java.util.List;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//import roomescape.application.ReservationService;
//import roomescape.presentation.dto.request.ReservationRequest;
//import roomescape.presentation.dto.response.ReservationResponse;
//
//@WebMvcTest(ReservationController.class)
//class ReservationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockitoBean
//    private ReservationService reservationService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("모든 예약을 조회할 수 있다")
//    void getAllReservations() throws Exception {
//        // given
//        ReservationResponse reservation = new ReservationResponse(1L, "John", LocalDate.of(2025, 5, 1), 1L);
//        given(reservationService.getAllReservations()).willReturn(List.of(reservation));
//
//        // when & then
//        mocki
//        mockMvc.perform(get("/reservations"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(1L))
//                .andExpect(jsonPath("$[0].name").value("John"));
//    }
//
//    @Test
//    @DisplayName("예약을 추가할 수 있다")
//    void addReservation() throws Exception {
//        // given
//        ReservationRequest request = new ReservationRequest("John", LocalDate.of(2025, 5, 1), 1L);
//        ReservationResponse response = new ReservationResponse(1L, "John", LocalDate.of(2025, 5, 1), 1L);
//
//        given(reservationService.registerReservation(request)).willReturn(response);
//
//        // when & then
//        mockMvc.perform(post("/reservations")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1L))
//                .andExpect(jsonPath("$.name").value("John"));
//    }
//
//    @Test
//    @DisplayName("예약을 삭제할 수 있다")
//    void deleteReservation() throws Exception {
//        // given
//        Long id = 1L;
//        doNothing().when(reservationService).deleteReservation(id);
//
//        // when & then
//        mockMvc.perform(delete("/reservations/{id}", id))
//                .andExpect(status().isOk());
//    }
//}
