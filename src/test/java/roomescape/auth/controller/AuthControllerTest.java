package roomescape.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.LoginResponse;
import roomescape.auth.service.AuthService;
import roomescape.common.DateTimeFormatConfiguration;

@ActiveProfiles("test")
@WebMvcTest({
        AuthController.class,
        DateTimeFormatConfiguration.class
})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("로그인 성공 시 토큰 값을 헤더에 담아 전달한다.")
    void login() throws Exception {
        // stub
        String tokenValue = "asdf";
        Mockito.when(authService.login(any()))
                .thenReturn(new LoginResponse(tokenValue));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .content(objectMapper.writeValueAsString(
                                new LoginRequest("asdf@adf.c", "asdf")
                        ))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        header().stringValues("Set-Cookie", "token=" + tokenValue),
                        status().isOk()
                );
    }
}
