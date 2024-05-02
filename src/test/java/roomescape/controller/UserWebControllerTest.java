package roomescape.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserWebController.class)
class UserWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("/로 요청하면 200응답이 넘어온다.")
    @Test
    void requestAdminPageTest() throws Exception {
        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("/reservation으로 요청하면 200응답이 넘어온다.")
    @Test
    void requestReservationPageTest() throws Exception {
        mockMvc.perform(get("/reservation"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
