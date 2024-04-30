package roomescape.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerTest extends ControllerTest {

    @Test
    @DisplayName("어드민 메인 페이지를 반환한다.")
    void mainPage() throws Exception {
        // whe & then
        mockMvc.perform(get("/admin"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("어드민 예약 페이지를 반환한다.")
    void reservationPage() throws Exception {
        // whe & then
        mockMvc.perform(get("/admin/reservation"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
