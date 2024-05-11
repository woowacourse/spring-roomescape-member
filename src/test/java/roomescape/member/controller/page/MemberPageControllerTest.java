package roomescape.member.controller.page;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import roomescape.auth.service.AuthService;
import roomescape.member.service.MemberService;

@WebMvcTest(MemberPageController.class)
class MemberPageControllerTest {

    @MockBean
    private MemberService memberService;
    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("일반 사용자가 reservation 페이지에 접근 할 수 있다.")
    @Test
    public void reservations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reservation"))
                .andExpect(status().isOk());
    }

    @DisplayName("일반 사용자가 login 페이지에 접근 할 수 있다.")
    @Test
    public void login() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk());
    }
}
