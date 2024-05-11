package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.application.AuthService;
import roomescape.controller.dto.LoginRequest;
import roomescape.support.ControllerTest;
import roomescape.support.SimpleMockMvc;

class AuthControllerTest extends ControllerTest {
    @Autowired
    private AuthService authService;

    @Test
    void 로그인을_성공한다() throws Exception {
        when(authService.createToken(any())).thenReturn("token!!!");
        LoginRequest request = new LoginRequest("abc@gmail.com", "1q2w3e4r!");
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/login", content);

        result.andExpectAll(
                        status().isOk(),
                        header().string("Set-Cookie", "token=token!!!; Path=/; HttpOnly")
                )
                .andDo(print());
    }
}
