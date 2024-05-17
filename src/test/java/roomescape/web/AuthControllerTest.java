package roomescape.web;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.member.Member;
import roomescape.web.api.token.TokenProvider;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TokenProvider tokenProvider;

    @DisplayName("로그인하면 토큰을 받는다")
    @Sql("/test-data/members.sql")
    @Test
    void when_login_then_getToken() throws Exception {
        // given
        String loginRequest = "{\"email\": \"pkpkpkpk@woowa.net\", \"password\": \"anything\"}";

        // when, then
        mockMvc.perform(post("/login")
                        .content(loginRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(header().string("Set-Cookie", containsString("token")));
    }

    @DisplayName("로그인에 실패하면 토큰이 없다")
    @Sql("/test-data/members.sql")
    @Test
    void when_loginFails_then_noToken() throws Exception {
        // given
        String loginRequest = "{\"email\": \"pkpkpkpk@woowa.net\", \"password\": \"wrongpassword\"}";

        // when, then
        mockMvc.perform(post("/login")
                        .content(loginRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Set-Cookie", not(containsString("token"))));
    }

    @DisplayName("토큰이 있으면 로그인 체크에 성공한다")
    @Sql("/test-data/members.sql")
    @Test
    void when_haveToken_then_checkLoginSucceed() throws Exception {
        // given
        String token = tokenProvider.createToken(new Member(1L, "피케이", "pkpkpkpk@woowa.net", "anything", "ADMIN"));

        // when, then
        mockMvc.perform(get("/login/check")
                        .cookie(new Cookie("token", token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("pkpkpkpk@woowa.net"));
    }

    @DisplayName("위조된 토큰이 있으면 로그인 체크에 실패한다")
    @Sql("/test-data/members.sql")
    @Test
    void when_haveExpiredToken_then_checkLoginFails() throws Exception {
        // given
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiTUVNQkVSIiwiZW1haWwiOiJwa3BrcGtwa0B3b293YS5uZXQifQ" +
                       ".o0PmXyH_dfrTl23OllhBVYJES5WhTMoG80SjpSz-nqU";

        // when, then
        mockMvc.perform(get("/login/check")
                        .cookie(new Cookie("token", token)))
                .andExpect(status().isUnauthorized());
    }

    @DisplayName("토큰이 없으면 로그인 체크에 실패한다")
    @Sql("/test-data/members.sql")
    @Test
    void when_haveNoToken_then_checkLoginFails() throws Exception {
        // when, then
        mockMvc.perform(get("/login/check"))
                .andExpect(status().isUnauthorized());
    }
}
