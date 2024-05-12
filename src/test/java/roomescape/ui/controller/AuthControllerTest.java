package roomescape.ui.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.application.AuthService;
import roomescape.application.dto.response.TokenResponse;
import roomescape.support.SimpleMockMvc;
import roomescape.ui.controller.dto.LoginRequest;

@WebMvcTest(AuthController.class)
class AuthControllerTest {
    @MockBean
    private AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void 로그인을_성공한다() throws Exception {
        when(authService.authenticateMember(any())).thenReturn(new TokenResponse("token!!!"));
        LoginRequest request = new LoginRequest("abc@gmail.com", "1q2w3e4r!");
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/login", content);

        result.andExpectAll(
                        status().isOk(),
                        header().string("Set-Cookie", "token=token!!!; Path=/; HttpOnly")
                )
                .andDo(print());
    }

    @Test
    void 로그아웃을_성공한다() throws Exception {
        ResultActions result = SimpleMockMvc.post(mockMvc, "/logout", "");

        result.andExpectAll(
                        status().isOk(),
                        header().string("Set-Cookie", StringContains.containsString("token=; Path=/; Max-Age=0;"))
                )
                .andDo(print());
    }
}
