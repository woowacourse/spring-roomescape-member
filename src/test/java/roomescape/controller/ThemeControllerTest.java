package roomescape.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static roomescape.TestFixture.*;

class ThemeControllerTest extends ControllerTest {

    @Test
    @DisplayName("테마 생성 POST 요청 시 상태코드 201을 반환한다.")
    void createTheme() throws Exception {
        // given
        ThemeSaveRequest request = new ThemeSaveRequest(THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);
        ThemeResponse expectedResponse = new ThemeResponse(1L, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

        BDDMockito.given(themeService.create(any()))
                .willReturn(expectedResponse);

        // when & then
        mockMvc.perform(post("/themes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(THEME_NAME))
                .andExpect(jsonPath("$.description").value(THEME_DESCRIPTION))
                .andExpect(jsonPath("$.thumbnail").value(THEME_THUMBNAIL))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("테마 목록 GET 요청 시 상태코드 200을 반환한다.")
    void findAllThemes() throws Exception {
        // given
        ThemeResponse expectedResponse = new ThemeResponse(1L, THEME_NAME, THEME_DESCRIPTION, THEME_THUMBNAIL);

        BDDMockito.given(themeService.findAll())
                .willReturn(List.of(expectedResponse));

        // when & then
        mockMvc.perform(get("/themes")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(THEME_NAME))
                .andExpect(jsonPath("$[0].description").value(THEME_DESCRIPTION))
                .andExpect(jsonPath("$[0].thumbnail").value(THEME_THUMBNAIL))
                .andExpect(jsonPath("$[0].id").value(1L));
    }
}
