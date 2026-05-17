package roomescape.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.domain.Theme;
import roomescape.domain.vo.ThemeImageUrl;
import roomescape.domain.vo.ThemeName;
import roomescape.dto.theme.ThemeRequest;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;
import roomescape.service.ThemeService;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest {

    private static final long THEME_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ThemeService themeService;

    @Test
    void 관리자는_테마를_추가할_수_있다() throws Exception {
        // given
        ThemeRequest request = themeRequestDtoFrom(theme());
        Theme savedTheme = theme().withId(THEME_ID);
        when(themeService.addTheme(any())).thenReturn(savedTheme);

        // when
        ResultActions result = mockMvc
            .perform(post("/themes")
                .queryParam("role", "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(THEME_ID));

        verify(themeService, times(1)).addTheme(any());
        verifyNoMoreInteractions(themeService);
    }

    @Test
    void 모든_테마를_조회한다() throws Exception {
        // given
        List<Theme> themes = List.of(
            theme().withId(1L), theme().withId(2L), theme().withId(3L));
        when(themeService.getThemes()).thenReturn(themes);

        // when
        ResultActions result = mockMvc.perform(get("/themes"));

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[2].id").value(3L));

        verify(themeService, times(1)).getThemes();
        verifyNoMoreInteractions(themeService);
    }

    @Test
    void 최근_1주일간_예약이_많은_테마_상위_10개를_조회할_수_있다() throws Exception {
        // given
        List<Theme> tenPopularThemes = createTenThemes();
        when(themeService.findWeekPopularThemesOrderByRank(10)).thenReturn(tenPopularThemes);

        // when
        ResultActions result = mockMvc
            .perform(get("/themes/popular/week")
                .queryParam("limit", "10"));

        // then
        result
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.themes", hasSize(10)));

        verify(themeService, times(1)).findWeekPopularThemesOrderByRank(anyInt());
        verifyNoMoreInteractions(themeService);
    }

    @Test
    void 관리자는_테마를_삭제할_수_있다() throws Exception {
        // given & when
        ResultActions result = mockMvc
            .perform(delete("/themes/{id}", THEME_ID)
                .queryParam("role", "admin"));

        // then
        result.andExpect(status().isNoContent());

        verify(themeService, times(1)).deleteThemeById(THEME_ID);
        verifyNoMoreInteractions(themeService);
    }

    @Test
    void 예약이_존재하는_테마를_삭제할_경우_예외_응답을_반환한다() throws Exception {
        // given
        doThrow(new RoomEscapeException(ErrorCode.THEME_HAS_RESERVATIONS))
            .when(themeService).deleteThemeById(THEME_ID);

        // when
        ResultActions result = mockMvc
            .perform(delete("/themes/{id}", THEME_ID)
                .queryParam("role", "admin"));

        // then
        result
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value(ErrorCode.THEME_HAS_RESERVATIONS.name()));

        verify(themeService, times(1)).deleteThemeById(THEME_ID);
        verifyNoMoreInteractions(themeService);
    }

    @Nested
    @DisplayName("인가 권한이 없는 경우 예외가 발생한다")
    class RoleForbidden {

        @Test
        void 관리자가_아닌_사용자가_테마를_추가하는_경우_예외가_발생한다() throws Exception {
            // given
            ThemeRequest request = themeRequestDtoFrom(theme());

            // when
            ResultActions result = mockMvc
                .perform(post("/themes")
                    .queryParam("role", "user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            result.andExpect(status().isForbidden());

            verifyNoMoreInteractions(themeService);
        }

        @Test
        void 관리자가_아닌_사용자가_테마를_삭제하는_경우_예외가_발생한다() throws Exception {
            // given & when
            ResultActions result = mockMvc
                .perform(delete("/themes/{id}", THEME_ID)
                    .queryParam("role", "user"));

            // then
            result.andExpect(status().isForbidden());

            verifyNoMoreInteractions(themeService);
        }
    }

    private Theme theme() {
        return new Theme(null, new ThemeName("name"), "description", ThemeImageUrl.defaultImageUrl());
    }

    private List<Theme> createTenThemes() {
        List<Theme> themes = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            themes.add(theme().withId(i));
        }
        return themes;
    }

    private ThemeRequest themeRequestDtoFrom(Theme theme) {
        return new ThemeRequest(
            theme.getNameValue(),
            theme.getDescription(),
            theme.getImageUrlValue()
        );
    }
}
