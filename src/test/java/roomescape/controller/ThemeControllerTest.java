package roomescape.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.Theme;
import roomescape.dto.SaveThemeRequest;
import roomescape.service.ThemeService;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ThemeService themeService;

    @DisplayName("전체 테마 정보를 조회한다.")
    @Test
    void getThemesTest() throws Exception {
        // Given
        final List<Theme> themes = List.of(
                Theme.of(1L, "켈리 탈출", "켈리와 탈출", "켈리 사진"),
                Theme.of(2L, "테바 탈출", "테봐와 탈출", "테바 사진"),
                Theme.of(3L, "커비 탈출", "커비와 탈출", "커비 사진")
        );

        given(themeService.getThemes()).willReturn(themes);

        // When & Then
        mockMvc.perform(get("/themes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @DisplayName("테마 정보를 저장한다.")
    @Test
    void saveThemeTest() throws Exception {
        final String themeName = "켈리의 탈출";
        final String themeDescription = "켈리와 탈출!";
        final String themeThumbnail = "켈리 사진";
        final SaveThemeRequest saveThemeRequest = new SaveThemeRequest(themeName, themeDescription, themeThumbnail);
        final Theme savedTheme = Theme.of(1L, themeName, themeDescription, themeThumbnail);

        given(themeService.saveTheme(saveThemeRequest)).willReturn(savedTheme);

        // When & Then
        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(saveThemeRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.themeId").value(1))
                .andExpect(jsonPath("$.name").value(themeName))
                .andExpect(jsonPath("$.description").value(themeDescription))
                .andExpect(jsonPath("$.thumbnail").value(themeThumbnail));
    }

    @DisplayName("예약 시간 정보를 삭제한다.")
    @Test
    void deleteThemeTest() throws Exception {
        // Given
        final Long themeId = 1L;
        willDoNothing().given(themeService).deleteTheme(themeId);

        // When & Then
        mockMvc.perform(delete("/themes/" + themeId))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}
