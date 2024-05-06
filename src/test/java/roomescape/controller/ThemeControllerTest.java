package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.application.ThemeService;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.fixture.ThemeFixture;
import roomescape.support.ControllerTest;
import roomescape.support.SimpleMockMvc;

class ThemeControllerTest extends ControllerTest {
    @Autowired
    private ThemeService themeService;

    @Test
    void 테마를_생성한다() throws Exception {
        Theme theme = ThemeFixture.theme(1L, "레모네와 함께 탐험", "설명", "https://lemone.com");
        when(themeService.save(any())).thenReturn(theme);
        ThemeRequest request = new ThemeRequest(theme.getName(), theme.getDescription(), theme.getThumbnail());
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/themes", content);

        result.andExpectAll(
                status().isCreated(),
                jsonPath("$.id").value(theme.getId()),
                jsonPath("$.name").value(theme.getName()),
                jsonPath("$.description").value(theme.getDescription()),
                jsonPath("$.thumbnail").value(theme.getThumbnail())
        ).andDo(print());
    }

    @Test
    void 테마명이_비어있으면_400_status를_반환한다() throws Exception {
        ThemeRequest request = new ThemeRequest(null, "설명", "https://lemone.com");
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/themes", content);

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.fieldErrors[0].field").value("name"),
                jsonPath("$.fieldErrors[0].rejectedValue").value(IsNull.nullValue()),
                jsonPath("$.fieldErrors[0].reason").value("테마명은 필수입니다.")
        ).andDo(print());
    }

    @Test
    void 테마_설명이_비어있으면_400_status를_반환한다() throws Exception {
        ThemeRequest request = new ThemeRequest("테마", "", "https://lemone.com");
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/themes", content);

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.fieldErrors[0].field").value("description"),
                jsonPath("$.fieldErrors[0].rejectedValue").value(""),
                jsonPath("$.fieldErrors[0].reason").value("테마 설명은 필수입니다.")
        ).andDo(print());
    }

    @ParameterizedTest
    @ValueSource(strings = {"https://lemone", "lemone.com", "www.lemone.com"})
    void 썸네일_URL_형식이_올바르지_않으면_400_status를_반환한다(String thumbnail) throws Exception {
        ThemeRequest request = new ThemeRequest("테마", "설명", thumbnail);
        String content = objectMapper.writeValueAsString(request);

        ResultActions result = SimpleMockMvc.post(mockMvc, "/themes", content);

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.fieldErrors[0].field").value("thumbnail"),
                jsonPath("$.fieldErrors[0].rejectedValue").value(thumbnail),
                jsonPath("$.fieldErrors[0].reason").value("URL 형식이 올바르지 않습니다.")
        ).andDo(print());
    }

    @Test
    void 전체_테마를_조회한다() throws Exception {
        List<Theme> themes = List.of(ThemeFixture.theme(), ThemeFixture.theme());
        when(themeService.getThemes()).thenReturn(themes);

        ResultActions result = SimpleMockMvc.get(mockMvc, "/themes");

        result.andExpectAll(
                status().isOk(),
                jsonPath("$[0].id").value(themes.get(0).getId()),
                jsonPath("$[1].id").value(themes.get(1).getId())
        ).andDo(print());
    }

    @Test
    void 테마를_삭제한다() throws Exception {
        long id = 1L;
        doNothing().when(themeService).delete(id);

        ResultActions result = SimpleMockMvc.delete(mockMvc, "/themes/{id}", id);

        result.andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void 테마_아이디가_양수가_아니면_400_status를_반환한다() throws Exception {
        long invalidId = -1L;

        ResultActions result = SimpleMockMvc.delete(mockMvc, "/themes/{id}", invalidId);

        result.andExpectAll(
                status().isBadRequest(),
                jsonPath("$.violationErrors[0].field").value("id"),
                jsonPath("$.violationErrors[0].rejectedValue").value(invalidId)
        ).andDo(print());
    }

    @Test
    void 인기_테마를_조회한다() throws Exception {
        List<Theme> popularThemes = List.of(ThemeFixture.theme(), ThemeFixture.theme());
        when(themeService.getPopularThemes()).thenReturn(popularThemes);

        ResultActions result = SimpleMockMvc.get(mockMvc, "/themes/popular");

        result.andExpectAll(
                status().isOk(),
                jsonPath("$[0].id").value(popularThemes.get(0).getId()),
                jsonPath("$[1].id").value(popularThemes.get(1).getId())
        ).andDo(print());
    }
}
