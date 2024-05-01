package roomescape.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTest {

    @Test
    @DisplayName("사용자 예약 페이지를 반환한다.")
    void reservationPage() throws Exception {
        // when & then
        mockMvc.perform(get("/reservation"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
