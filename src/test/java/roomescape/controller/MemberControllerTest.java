package roomescape.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.member.Member;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.LoginResponse;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.MemberSignupRequest;
import roomescape.exception.AuthorizationException;
import roomescape.fixture.MemberFixtures;
import roomescape.service.AuthService;
import roomescape.service.MemberService;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberService memberService;
    @MockBean
    private AuthService authService;


    @Test
    @DisplayName("모든 회원 정보를 조회한다.")
    void findAll() throws Exception {
        //given
        List<MemberResponse> memberResponses = List.of(
                MemberFixtures.createMemberResponse(1L, "user1", "user1@test.com"),
                MemberFixtures.createMemberResponse(2L, "user2", "user1@test.com")
        );
        given(memberService.findAll()).willReturn(memberResponses);

        //when //then
        mockMvc.perform(get("/members"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("user1")))
                .andExpect(jsonPath("$[1].name", is("user2")));
    }

    @Test
    @DisplayName("회원 가입을 요청하면 201 Created를 응답한다.")
    void signup() throws Exception {
        //given
        MemberSignupRequest request = new MemberSignupRequest("test", "test@test.com", "1234");
        MemberResponse response = MemberFixtures.createMemberResponse(1L, "test", "test@test.com");
        given(memberService.add(any(MemberSignupRequest.class))).willReturn(response);
        String jsonRequest = objectMapper.writeValueAsString(request);

        //when //then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("test")))
                .andExpect(jsonPath("$.email", is("test@test.com")));
    }

    @Test
    @DisplayName("회원 가입 요청시 예외가 발생하면 400 Bad Request를 응답한다.")
    void signUpWithException() throws Exception {
        //given
        MemberSignupRequest request = new MemberSignupRequest("test", "test@test.com", "1234");
        given(memberService.add(any(MemberSignupRequest.class))).willThrow(IllegalArgumentException.class);
        String jsonRequest = objectMapper.writeValueAsString(request);

        //when //then
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("로그인을 성공하면 200 ok와 sessionId를 반환한다.")
    void login() throws Exception {
        //given
        String accessToken = "accessToken";
        LoginRequest loginRequest = new LoginRequest("email", "1234");
        doNothing().when(memberService).checkLoginInfo(any(LoginRequest.class));
        given(authService.createToken(any(LoginRequest.class))).willReturn(new LoginResponse(accessToken));
        String request = objectMapper.writeValueAsString(loginRequest);

        //when //then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", "token=" + accessToken + "; Path=/; HttpOnly"));
    }

    @Test
    @DisplayName("로그인을 실패하면 401 Unauthorized를 반환한다.")
    void loginWhenFail() throws Exception {
        //given
        LoginRequest loginRequest = new LoginRequest("email", "1234");
        doNothing().when(memberService).checkLoginInfo(any(LoginRequest.class));
        given(authService.createToken(any(LoginRequest.class))).willThrow(AuthorizationException.class);
        String request = objectMapper.writeValueAsString(loginRequest);

        //when //then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("로그인 검증을 성공하면 200 OK와 사용자명을 응답한다.")
    void loginCheck() throws Exception {
        //given
        Cookie cookie = new Cookie("token", "1234");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        String name = "daon";
        String email = "test@test.com";
        Member member = MemberFixtures.createUserMember(name, email);
        given(authService.findPayload(anyString())).willReturn(email);
        given(memberService.findAuthInfo(anyString())).willReturn(member);

        //when //then
        mockMvc.perform(get("/login/check")
                        .cookie(cookie))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)));
    }

    @Test
    @DisplayName("로그인 검증을 실패하면 401 UnAuthorized를 응답한다.")
    void loginCheckWhenFail() throws Exception {
        //given
        Cookie cookie = new Cookie("token", "1234");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        given(authService.findPayload(anyString())).willThrow(AuthorizationException.class);
        given(memberService.findAuthInfo(anyString())).willThrow(IllegalArgumentException.class);

        //when //then
        mockMvc.perform(get("/login/check")
                        .cookie(cookie))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
