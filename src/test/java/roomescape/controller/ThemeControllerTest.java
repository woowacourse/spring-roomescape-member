package roomescape.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import roomescape.application.ThemeService;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.support.ControllerTest;
import roomescape.support.SimpleMockMvc;

class ThemeControllerTest extends ControllerTest {
    @Autowired
    private ThemeService themeService;

    @Test
    void 테마를_생성한다() throws Exception {
        Theme theme = new Theme(1L, "레모네와 함께 탐험", "설명", "https://lemone.com");
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
    void 전체_테마를_조회한다() throws Exception {
        List<Theme> themes = List.of(new Theme(1L, "테마1", "설명1", "링크1"),
                new Theme(2L, "테마2", "설명2", "링크2"));
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
}
