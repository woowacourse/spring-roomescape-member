package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.config.WebMvcControllerTestConfig;

@WebMvcTest(ReservationPageController.class)
@Import(WebMvcControllerTestConfig.class)
class ReservationPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/reservation 를 요청하면 reservation.html 를 반환한다.")
    void requestUserReservation() throws Exception {
        mockMvc.perform(get("/reservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"));
    }

    @Test
    @DisplayName("/admin/reservation 를 요청하면 admin/reservation-new.html 를 반환한다.")
    void requestAdminReservation() throws Exception {
        mockMvc.perform(get("/admin/reservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reservation-new"));
    }
}
