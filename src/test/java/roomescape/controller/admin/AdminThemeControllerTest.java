package roomescape.controller.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.service.ThemeService;

@WebMvcTest(AdminThemeController.class)
class AdminThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThemeService themeService;

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "         "})
    @DisplayName("테마 이름이 공백인 경우 테마 생성에 실패한다.")
    void failCreate_When_ThemeNameIsBlank(String name) throws Exception {
        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "%s",
                                  "description": "설명",
                                  "thumbnailUrl": "https://example.com/image.jpg"
                                }
                                """.formatted(name)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("테마 설명이 공백인 경우 테마 생성에 실패한다.")
    void failCreate_When_ThemeDescriptionIsBlank() throws Exception {
        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "테마",
                                  "description": "",
                                  "thumbnailUrl": "https://example.com/image.jpg"
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("썸네일 URL이 공백인 경우 테마 생성에 실패한다.")
    void failCreate_When_ThumbnailUrlIsBlank() throws Exception {
        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "테마",
                                  "description": "설명",
                                  "thumbnailUrl": ""
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {"유용하지않은URL", "example.com/image.jpg", "image.jpg"})
    @DisplayName("썸네일 URL 형식이 올바르지 않은 경우 테마 생성에 실패한다.")
    void failCreate_When_ThumbnailUrlIsInvalid(String thumbnailUrl) throws Exception {
        mockMvc.perform(post("/admin/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "테마",
                                  "description": "설명",
                                  "thumbnailUrl": "%s"
                                }
                                """.formatted(thumbnailUrl)))
                .andExpect(status().isBadRequest());
    }
}
