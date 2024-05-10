package roomescape.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.auth.dto.response.GetAuthInfoResponse;
import roomescape.auth.dto.response.LoginResponse;
import roomescape.auth.service.AuthService;
import roomescape.testutil.ControllerTest;

@ControllerTest
@Import(AuthController.class)
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
                        cookie().value("token", tokenValue),
                        status().isOk()
                );
    }

    @Test
    @DisplayName("인증 정보 조회 시 요청 받은 쿠키의 토큰 값으로부터 유저 정보를 반환한다.")
    void checkLogin() throws Exception {
        // stub
        Mockito.when(authService.getMemberAuthInfo(any())).thenReturn(new GetAuthInfoResponse("hi"));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/login/check")
                        .cookie(new Cookie("token", "asdf"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$.name").value("hi"),
                        status().isOk()
                );
    }

    @Test
    @DisplayName("인증 정보 조회 시 토큰 정보를 담은 쿠키가 없는 경우, 예외를 반환한다.")
    void checkLogin_WhenRequestNotContainsCookie() throws Exception {
        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/login/check")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$").value(
                                "쿠키에 저장된 인증 토큰 값이 비어있습니다. 로그인 후 다시 시도해주세요."),
                        status().isUnauthorized()
                );
    }
}
