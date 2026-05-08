package roomescape.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeWithCount;
import roomescape.service.ThemeService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThemeService themeService;

    private Theme theme;

    @BeforeEach
    void setUp() {
        theme = new Theme(1L, "테마1", "테마 설명", "image url");
    }

    @Test
    @DisplayName("테마 목록 조회 시 200과 바디를 반환한다")
    void getThemes() throws Exception {
        given(themeService.getAllTheme())
                .willReturn(List.of(theme));

        mockMvc.perform(get("/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("테마1"))
                .andExpect(jsonPath("$[0].description").value("테마 설명"))
                .andExpect(jsonPath("$[0].imageUrl").value("image url"));
    }

    @Test
    @DisplayName("테마 추가 시 201과 바디를 반환한다")
    void addTheme() throws Exception {
        given(themeService.addTheme(any()))
                .willReturn(theme);

        String requestBody = """
            {
                "name": "테마1",
                "description": "테마 설명",
                "imageUrl": "image url"
            }
        """;

        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("테마1"))
                .andExpect(jsonPath("$.description").value("테마 설명"))
                .andExpect(jsonPath("$.imageUrl").value("image url"));
    }

    @Test
    @DisplayName("필수 값 없이 테마 추가 시 400을 반환한다")
    void addThemeWithInvalidRequest() throws Exception {
        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("테마 삭제 시 204를 반환한다")
    void deleteTheme() throws Exception {
        mockMvc.perform(delete("/themes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("인기 테마 조회 시 200과 바디를 반환한다")
    void getPopularTheme() throws Exception {
        ThemeWithCount themeWithCount = new ThemeWithCount(1L, "테마1", "테마 설명", "image url", 5L);
        given(themeService.getPopularTheme(any()))
                .willReturn(List.of(themeWithCount));

        mockMvc.perform(get("/themes/popular")
                        .param("startDate", "2026-04-01")
                        .param("endDate", "2026-05-01")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("테마1"))
                .andExpect(jsonPath("$[0].count").value(5));
    }

    @Test
    @DisplayName("잘못된 날짜 형식으로 인기 테마 조회 시 400을 반환한다")
    void getPopularThemeWithInvalidDate() throws Exception {
        mockMvc.perform(get("/themes/popular")
                        .param("startDate", "20260401")  // 잘못된 형식
                        .param("endDate", "2026-05-01")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }
}
