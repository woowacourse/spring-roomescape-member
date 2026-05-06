package roomescape.reservationtime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminReservationTimeController.class)
class AdminReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdminReservationTimeService reservationTimeService;

    @Test
    void 예약_시간을_생성할_수_있다() throws Exception {
        ReservationTime saved = new ReservationTime(1L, LocalTime.of(10, 0));
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));

        when(reservationTimeService.createReservationTime(eq(LocalTime.of(10, 0)))).thenReturn(saved);

        mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.startAt").value("10:00:00"));
    }

    @Test
    void 예약_시간을_삭제할_수_있다() throws Exception {
        mockMvc.perform(delete("/admin/times/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
