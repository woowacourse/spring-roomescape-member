package roomescape.validation;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.global.exception.GlobalExceptionHandler;
import roomescape.theme.controller.ThemeAdminController;
import roomescape.theme.service.ThemeService;

@WebMvcTest(ThemeAdminController.class)
@Import(GlobalExceptionHandler.class)
class ThemeValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ThemeService themeService;

    @Test
    @DisplayName("테마 생성 시 이름이 비어 있으면 검증 예외")
    void create_whenNameBlank_throws() throws Exception {
        String requestBody = """
                {
                  "name": "",
                  "description": "설명",
                  "thumbnailUrl": "https://example.com/theme.png"
                }
                """;

        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.validationErrors[0]").value("이름은 비어 있을 수 없습니다."));

        verifyNoInteractions(themeService);
    }
}
