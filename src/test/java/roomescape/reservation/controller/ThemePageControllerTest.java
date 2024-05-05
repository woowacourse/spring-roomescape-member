package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ThemePageController.class)
class ThemePageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/theme 을 요청하면 admin/theme.html 를 반환한다.")
    void requestTheme() throws Exception {
        mockMvc.perform(get("/admin/theme"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/theme"));
    }
}
