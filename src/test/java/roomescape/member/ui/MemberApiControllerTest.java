package roomescape.member.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.auth.application.AuthService;
import roomescape.auth.domain.Token;
import roomescape.member.application.MemberService;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.LoginRequest;
import roomescape.member.dto.MemberResponse;

@WebMvcTest(MemberApiController.class)
class MemberApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원 목록을 조회할 수 있다")
    void test1() throws Exception {
        // given
        MemberResponse response = new MemberResponse(1L, "미미");
        when(memberService.findAll()).thenReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("미미"));
    }

    @Test
    @DisplayName("쿠키에서 token을 추출하여 인증된 사용자 정보를 전달한다")
    void test2() throws Exception {
        // given
        Member member = new Member(1L, "미미", "mimi@email.com", "password", Role.MEMBER);
        Token token = new Token("valid-token");
        when(authService.findMemberByToken(any(Token.class))).thenReturn(member);

        // when & then
        mockMvc.perform(get("/login/check")
                        .cookie(new Cookie("token", token.value())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("미미"));
    }

    @Test
    @DisplayName("로그인하면 토큰을 응답 쿠키에 담아서 반환한다")
    void test3() throws Exception {
        // given
        LoginRequest request = new LoginRequest("mimi@email.com", "password");
        Token token = new Token("valid-token");

        when(authService.login(any(LoginRequest.class))).thenReturn(token);

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));
    }
}
