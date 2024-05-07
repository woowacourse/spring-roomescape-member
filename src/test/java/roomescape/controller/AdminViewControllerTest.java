package roomescape.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminViewController.class)
class AdminViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("홈 화면을 요청하면 200 OK을 응답한다.")
    void adminPageTest() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("예약 관리 페이지를 요청하면 200 OK를 반환한다.")
    void reservationPageTest() throws Exception {
        mockMvc.perform(get("/admin/reservation"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("테마 관리 페이지를 요청하면 200 OK를 반환한다.")
    void themePageTest() throws Exception {
        mockMvc.perform(get("/admin/theme"))
                .andExpect(status().isOk());
    }
}
