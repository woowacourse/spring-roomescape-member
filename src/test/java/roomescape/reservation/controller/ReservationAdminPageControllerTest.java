package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReservationAdminPageController.class)
public class ReservationAdminPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/admin 을 요청하면 index.html 를 반환한다.")
    void requestAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/index"));
    }

    @Test
    @DisplayName("/admin/reservation 를 요청하면 admin/reservation-new.html 를 반환한다.")
    void requestAdminReservation() throws Exception {
        mockMvc.perform(get("/admin/reservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/reservation-new"));
    }

    @Test
    @DisplayName("/theme 을 요청하면 admin/theme.html 를 반환한다.")
    void requestTheme() throws Exception {
        mockMvc.perform(get("/admin/theme"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/theme"));
    }

    @Test
    @DisplayName("/time 을 요청하면 time.html 를 반환한다.")
    void requestTime() throws Exception {
        mockMvc.perform(get("/admin/time"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/time"));
    }
}
