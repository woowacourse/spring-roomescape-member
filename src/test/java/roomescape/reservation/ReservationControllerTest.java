package roomescape.reservation;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomescapeException;

@WebMvcTest(ReservationController.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void 예약_조회() throws Exception {
        given(reservationService.read()).willReturn(List.of());

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk());
    }

    @Test
    void 예약_추가() throws Exception {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("userName", "매트");
        reservation.put("themeId", 1);
        reservation.put("date", "2026-05-10");
        reservation.put("timeId", 1);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isCreated());
    }

    @Test
    void 예약_삭제() throws Exception {
        willDoNothing().given(reservationService).delete(1L);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void 빈값으로_예약_추가시_400() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("userName", "");
        params.put("date", "2026-05-10");
        params.put("timeId", 1);
        params.put("themeId", 1);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 존재하지_않는_예약_삭제시_404() throws Exception {
        doThrow(new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND))
                .when(reservationService).delete(0L);

        mockMvc.perform(delete("/api/reservations/0"))
                .andExpect(status().isNotFound());
    }
}
