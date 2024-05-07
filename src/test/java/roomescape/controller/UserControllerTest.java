package roomescape.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest extends ControllerTest {

    @Test
    @DisplayName("사용자 메인 페이지를 반환한다.")
    void mainPage() throws Exception {
        // when & then
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("/index"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andDo(print());
    }

    @Test
    @DisplayName("사용자 예약 페이지를 반환한다.")
    void reservationPage() throws Exception {
        // when & then
        mockMvc.perform(get("/reservation"))
                .andExpect(status().isOk())
                .andExpect(view().name("/reservation"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andDo(print());
    }

    @Test
    @DisplayName("사용자 로그인 페이지를 반환한다.")
    void loginPage() throws Exception {
        // when & then
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/login"))
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andDo(print());
    }
}
