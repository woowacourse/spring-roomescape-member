package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.config.WebMvcControllerTestConfig;

@WebMvcTest(ReservationTimePageController.class)
@Import(WebMvcControllerTestConfig.class)
class ReservationTimePageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/time 을 요청하면 time.html 를 반환한다.")
    void requestTime() throws Exception {
        mockMvc.perform(get("/admin/time")
                        .cookie(new Cookie("token", "cookieValue")))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/time"));
    }
}
