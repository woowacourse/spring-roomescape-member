package roomescape.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends ControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    void checkLogin() {
    }
}
