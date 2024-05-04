package roomescape.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest extends ControllerTest {

    @Test
    @DisplayName("사용자 메인 페이지를 반환한다.")
    void mainPage() throws Exception {
        // when & then
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자 예약 페이지를 반환한다.")
    void reservationPage() throws Exception {
        // when & then
        mockMvc.perform(get("/reservation"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
