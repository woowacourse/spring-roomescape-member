package roomescape.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ViewController.class)
class ViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void 홈_페이지를_반환한다() throws Exception {
        // when & then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    void 예약_페이지를_반환한다() throws Exception {
        // when & then
        mockMvc.perform(get("/reservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("reservation"));
    }

    @Test
    void 관리자_예약_페이지를_반환한다() throws Exception {
        // when & then
        mockMvc.perform(get("/admin/reservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-reservation"));
    }

    @Test
    void 관리자_시간_페이지를_반환한다() throws Exception {
        // when & then
        mockMvc.perform(get("/admin/time"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-time"));
    }

    @Test
    void 관리자_테마_페이지를_반환한다() throws Exception {
        // when & then
        mockMvc.perform(get("/admin/theme"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin-theme"));
    }
}
