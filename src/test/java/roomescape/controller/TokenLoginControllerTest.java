package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.application.AuthService;
import roomescape.controller.api.TokenLoginController;
import roomescape.dto.auth.TokenRequest;
import roomescape.dto.auth.TokenResponse;

@WebMvcTest(TokenLoginController.class)
@Sql("/member.sql")
public class TokenLoginControllerTest {
    @Autowired
    private AuthService authService;
    @MockBean
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 로그인에_성공한다() throws Exception {
        when(authService.createToken(any())).thenReturn(new TokenResponse("테스트 토큰"));
        TokenRequest request = new TokenRequest("lemone1234", "lemone@wooteco.com");

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)));

        result.andExpectAll(
                status().isOk(),
                content().contentType("application/json"),
                header().stringValues("Keep-Alive", "timeout=60"),
                header().stringValues("Set-Cookie", "테스트 토큰"),
                header().exists("Path=/; HttpOnly")

        ).andDo(print());
    }
}
