package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservationTime.CreateReservationTimeRequest;
import roomescape.service.ReservationTimeService;

@WebMvcTest(AdminReservationTimeController.class)
class AdminReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReservationTimeService reservationTimeService;

    @Test
    void POST_admin_times_생성된_id를_Location_헤더에_담아_201을_반환한다() throws Exception {
        given(reservationTimeService.createReservationTime(any(CreateReservationTimeRequest.class)))
                .willReturn(new ReservationTime(5L, LocalTime.of(10, 0)));

        Map<String, Object> body = Map.of("startAt", "10:00");

        mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/times/5"));
    }

    @Test
    void DELETE_admin_times_id_200을_반환하고_서비스에_위임한다() throws Exception {
        mockMvc.perform(delete("/admin/times/3"))
                .andExpect(status().isOk());

        verify(reservationTimeService).deleteReservationTime(3L);
    }
}