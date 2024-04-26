package roomescape.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mvc;

    @DisplayName("/admin 페이지를 요청할 수 있다.")
    @Test
    void getIndexPage() throws Exception {
        mvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @DisplayName("/admin/reservation 페이지를 요청할 수 있다.")
    @Test
    void getReservationPage() throws Exception {
        mvc.perform(get("/admin/reservation"))
                .andExpect(status().isOk());
    }

    @DisplayName("/admin/time 페이지를 요청할 수 있다.")
    @Test
    void getReservationTimePage() throws Exception {
        mvc.perform(get("/admin/time"))
                .andExpect(status().isOk());
    }
}
