package roomescape.reservation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
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
import roomescape.reservation.dto.ReservationResponse;

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
        reservation.put("date", "2026-12-31");
        reservation.put("timeId", 1);

        given(reservationService.create(any()))
                .willReturn(new ReservationResponse(1L, "매트", null,
                        LocalDate.of(2026, 12, 31), null));

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
        params.put("themeId", 1);
        params.put("date", "2026-12-31");
        params.put("timeId", 1);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 존재하지_않는_예약_삭제시_404() throws Exception {
        willThrow(new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND))
                .given(reservationService).delete(0L);

        mockMvc.perform(delete("/api/reservations/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void 이미_존재하는_예약_추가시_409() throws Exception {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("userName", "매트");
        reservation.put("themeId", 1);
        reservation.put("date", "2026-05-10");
        reservation.put("timeId", 1);

        willThrow(new RoomescapeException(ErrorCode.RESERVATION_DUPLICATE))
                .given(reservationService).create(any());

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isConflict());
    }
}
