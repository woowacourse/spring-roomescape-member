package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.LoginResponse;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.MemberSignupRequest;
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
    @DisplayName("회원 가입을 요청하면 201 Created를 응답한다.")
    void signup() throws Exception {
        //given
        MemberSignupRequest request = new MemberSignupRequest("test", "test@test.com", "1234");
        MemberResponse response = new MemberResponse(1L, "test", "test@test.com");
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
}
