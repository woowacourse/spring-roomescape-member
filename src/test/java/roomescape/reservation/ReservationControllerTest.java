package roomescape.reservation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.exception.ForbiddenException;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.reservation.dto.ReservationsResponse;
import roomescape.theme.dto.ThemeResponse;
import roomescape.time.dto.ReservationTimeResponse;

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
        int page = 0;
        int size = 10;
        given(reservationService.read(page, size)).willReturn(ReservationsResponse.from(List.of()));

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
    void 예약_변경() throws Exception {
        ThemeResponse themeResponse = new ThemeResponse(1L, "공포의 방", "무서운 방", "http://test.com");
        ReservationTimeResponse timeResponse = new ReservationTimeResponse(2L, LocalTime.of(11, 0));
        ReservationResponse reservationResponse = new ReservationResponse(
                1L, "동키", themeResponse, LocalDate.of(2026, 6, 1), timeResponse
        );
        given(reservationService.update(anyLong(), any(), anyString()))
                .willReturn(reservationResponse);

        Map<String, Object> body = new HashMap<>();
        body.put("userName", "동키");
        body.put("themeId", 1);
        body.put("date", "2026-06-01");
        body.put("timeId", 2);

        mockMvc.perform(put("/api/reservations/1")
                        .header("X-User-Name", "동키")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void 예약_삭제() throws Exception {
        String decodeUserName = URLDecoder.decode("동키", StandardCharsets.UTF_8);
        willDoNothing().given(reservationService).delete(1L, "동키");

        mockMvc.perform(delete("/api/reservations/1")
                        .header("X-User-Name", decodeUserName))
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
    void 헤더_없이_삭제_요청시_400() throws Exception {
        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 다른_사용자_예약_삭제_요청시_403() throws Exception {
        willThrow(new ForbiddenException("본인의 예약만 삭제할 수 있습니다."))
                .given(reservationService).delete(anyLong(), anyString());

        mockMvc.perform(delete("/api/reservations/1")
                        .header("X-User-Name", "그해"))
                .andExpect(status().isForbidden());
    }
}
