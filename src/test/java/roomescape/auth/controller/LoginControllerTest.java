package roomescape.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.auth.dto.TokenRequest;
import roomescape.member.service.MemberService;
import roomescape.util.config.WebMvcTestConfig;

@Import(WebMvcTestConfig.class)
@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("로그인 요청이 올바르지 않으면 400을 반환한다")
    @CsvSource(
            value = {
                    "null, password123, email, 이메일을 입력해 주세요.",
                    "email123@email.com, null, password, 비밀번호를 입력해 주세요.",
                    "invalidEmail, password123, email, 올바른 이메일 형식을 입력해 주세요."
            },
            nullValues = "null"
    )
    @ParameterizedTest
    void login_validate_test(String email, String password, String errorFieldName, String errorMessage)
            throws Exception {
        // given
        TokenRequest tokenRequest = new TokenRequest(email, password);

        // when & then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$." + errorFieldName).value(errorMessage));
    }

    @TestConfiguration
    static class MockConfig {

        @Bean
        public MemberService memberService() {
            return Mockito.mock(MemberService.class);
        }

    }

}
