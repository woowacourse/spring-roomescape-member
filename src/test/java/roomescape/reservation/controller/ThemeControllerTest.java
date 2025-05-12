package roomescape.reservation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
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
import roomescape.auth.controller.LoginController;
import roomescape.auth.service.AuthService;
import roomescape.reservation.controller.dto.ThemeRequest;
import roomescape.reservation.service.ThemeService;
import roomescape.util.config.WebMvcTestConfig;
import roomescape.util.fixture.AuthFixture;

@Import(WebMvcTestConfig.class)
@WebMvcTest(ThemeController.class)
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @DisplayName("테마 생성 요청이 올바르지 않으면 400을 반환한다")
    @CsvSource(
            value = {
                    " , 설명, 대표 이미지, name, 테마 이름을 입력해주세요.",
                    " 이름, , 대표 이미지, description, 테마 설명을 입력해주세요.",
                    "이름, 설명, , thumbnail, 대표 이미지를 입력해주세요.",
                    "null, 설명, 대표 이미지, name, 테마 이름을 입력해주세요.",
                    " 이름, null, 대표 이미지, description, 테마 설명을 입력해주세요.",
                    "이름, 설명, null, thumbnail, 대표 이미지를 입력해주세요."
            },
            nullValues = "null"
    )
    @ParameterizedTest
    void add_theme_validate_test(String name, String description, String thumbnail, String errorFieldName,
                                 String errorMessage) throws Exception {
        // given
        String adminToken = AuthFixture.createAdminToken(authService);
        ThemeRequest request = new ThemeRequest(name, description, thumbnail);

        // when & then
        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie(LoginController.TOKEN_COOKIE_NAME, adminToken))
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$." + errorFieldName).value(errorMessage));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ThemeService themeService() {
            return Mockito.mock(ThemeService.class);
        }
    }

}
