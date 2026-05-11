package roomescape.time;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReservationTimeController.class)
class ReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationTimeService reservationTimeService;

    @Test
    void 예약시간_조회() throws Exception {
        given(reservationTimeService.read()).willReturn(List.of());

        mockMvc.perform(get("/api/times"))
                .andExpect(status().isOk());
    }

    @Test
    void 예약_가능한_시간_조회() throws Exception {
        given(reservationTimeService.readAvailableTimes(eq(1L), any(LocalDate.class)))
                .willReturn(List.of());

        mockMvc.perform(get("/api/times/availability")
                        .param("theme_id", "1")
                        .param("date", "2026-05-10"))
                .andExpect(status().isOk());
    }
}
