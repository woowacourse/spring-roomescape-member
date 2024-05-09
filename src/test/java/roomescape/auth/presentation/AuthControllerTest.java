package roomescape.auth.presentation;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import roomescape.auth.application.AuthService;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.common.ControllerTest;
import roomescape.auth.exception.IllegalTokenException;
import roomescape.auth.exception.AuthorizationException;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static roomescape.TestFixture.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends ControllerTest {
    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("로그인 요청 시 상태코드 200을 반환한다.")
    void login() throws Exception {
        // given
        String expectedToken = "token";
        LoginRequest request = new LoginRequest(MIA_EMAIL, TEST_PASSWORD);

        BDDMockito.given(authService.createToken(any()))
                .willReturn(expectedToken);

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.SET_COOKIE, "token=" + expectedToken));
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 요청 시 상태코드 401을 반환한다.")
    void loginWithNotExistingEmail() throws Exception {
        // given
        LoginRequest request = new LoginRequest("not existing email", TEST_PASSWORD);

        BDDMockito.willThrow(new AuthorizationException(TEST_ERROR_MESSAGE))
                .given(authService)
                .createToken(any());

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("인증 정보 GET 요청 시 상태코드 200을 반환한다.")
    void checkAuthInformation() throws Exception {
        // given
        Cookie cookie = new Cookie("token", "token");

        BDDMockito.given(authService.extractMember(any()))
                .willReturn(USER_MIA());

        // when & then
        mockMvc.perform(get("/login/check")
                        .cookie(cookie))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(MIA_NAME));
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 인증 정보 GET 요청 시 상태코드 401을 반환한다.")
    void checkAuthInformationWithInvalidToken() throws Exception {
        // given
        Cookie cookie = new Cookie("token", "invalid-token");

        BDDMockito.willThrow(new IllegalTokenException(TEST_ERROR_MESSAGE))
                .given(authService)
                .extractMember(any());

        // when & then
        mockMvc.perform(get("/login/check")
                        .cookie(cookie))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("토큰 없이 인증 정보 GET 요청 시 상태코드 401을 반환한다.")
    void checkAuthInformationWithoutToken() throws Exception {
        // when & then
        mockMvc.perform(get("/login/check"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }
}
