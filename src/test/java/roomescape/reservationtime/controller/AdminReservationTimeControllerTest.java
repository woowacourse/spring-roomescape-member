package roomescape.reservationtime.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.service.AdminReservationTimeService;

import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminReservationTimeController.class)
class AdminReservationTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminReservationTimeService adminReservationTimeService;

    @Test
    void 예약_시간을_정상적으로_생성한다() throws Exception {
        given(adminReservationTimeService.createReservationTime(any()))
                .willReturn(new ReservationTime(1L, LocalTime.of(10, 0)));

        mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"startAt":"10:00:00"}
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void 예약시간_생성시_startAt이_없으면_400을_반환한다() throws Exception {
        mockMvc.perform(post("/admin/times")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
