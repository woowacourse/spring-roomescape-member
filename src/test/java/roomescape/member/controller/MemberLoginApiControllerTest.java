package roomescape.member.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberSignUpRequest;
import roomescape.member.service.MemberLoginService;
import roomescape.member.service.MemberSignUpService;

@WebMvcTest(MemberLoginApiController.class)
class MemberLoginApiControllerTest {

    @MockBean
    private MemberSignUpService memberSignUpService;

    @MockBean
    private MemberLoginService memberLoginService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("로그인에 성공하면 200 응답을 받고 쿠키가 존재하는지 확인 후 로그인 체크 한다.")
    @Test
    void loginAndCheck() throws Exception {
        String name = "카키";
        String email = "kaki@email.com";
        String password = "1234";
        MemberSignUpRequest memberSignUpRequest = new MemberSignUpRequest(name, email, password);

        doReturn(1L).when(memberSignUpService)
                .save(memberSignUpRequest);

        MemberLoginRequest memberLoginRequest = new MemberLoginRequest(email, password);
        doReturn("validToken").when(memberLoginService)
                .createMemberToken(memberLoginRequest);

        MvcResult loginResult = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberLoginRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(cookie().exists(MemberLoginApiController.COOKIE_TOKEN_KEY))
                .andExpect(cookie().httpOnly(MemberLoginApiController.COOKIE_TOKEN_KEY, true))
                .andExpect(cookie().maxAge(MemberLoginApiController.COOKIE_TOKEN_KEY, MemberLoginApiController.COOKIE_MAX_AGE))
                .andReturn();

        mockMvc.perform(get("/login/check")
                        .cookie(loginResult.getResponse().getCookies()))
                .andExpect(status().isOk());
    }
}
