package roomescape.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.MediaType;
import roomescape.dto.ThemeResponse;
import roomescape.dto.ThemeSaveRequest;

import static org.mockito.ArgumentMatchers.any;
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
}
