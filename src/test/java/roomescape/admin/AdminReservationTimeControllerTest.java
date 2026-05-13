package roomescape.admin;

import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.time.ReservationTimeService;

@WebMvcTest(AdminReservationTimeController.class)
class AdminReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약시간_추가() throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "20:00");

        mockMvc.perform(post("/api/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isCreated());
    }

    @Test
    void 예약시간_삭제() throws Exception {
        willDoNothing()
                .given(reservationTimeService).delete(1L);

        mockMvc.perform(delete("/api/admin/times/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void 빈값으로_예약시간_추가시_400() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", null);

        mockMvc.perform(post("/api/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest());
    }
}
