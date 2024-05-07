package roomescape.controller.theme;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import roomescape.controller.ControllerTest;
import roomescape.service.ThemeService;
import roomescape.service.exception.ThemeNotFoundException;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest extends ControllerTest {

    @Autowired
    ThemeController themeController;

    @MockBean
    ThemeService themeService;

    @Test
    @DisplayName("테마 목록을 조회하면 200 과 테마 리스트를 응답한다.")
    void getThemes200AndThemes() throws Exception {
        // given
        final List<ThemeResponse> expectResponses = List.of(
                new ThemeResponse(1L, "Theme 1", "Description 1", "Thumbnail 1"),
                new ThemeResponse(2L, "Theme 2", "Description 2", "Thumbnail 2")
        );
        final String expectJson = objectMapper.writeValueAsString(expectResponses);

        Mockito.when(themeService.getThemes())
                .thenReturn(expectResponses);

        // when & then
        mvc.perform(get("/themes"))
                .andExpect(status().isOk())
                .andExpect(content().json(expectJson));
    }

    @Test
    @DisplayName("테마를 추가하면 201 과 테마와 위치를 응답한다.")
    void addTheme201AndThemeAndLocation() throws Exception {
        // given
        final CreateThemeRequest request = new CreateThemeRequest("Theme 1", "Description 1", "Thumbnail 1");
        final String requestJson = objectMapper.writeValueAsString(request);
        final ThemeResponse response = new ThemeResponse(1L, request.name(), request.description(), request.thumbnail());
        final String responseJson = objectMapper.writeValueAsString(response);

        Mockito.when(themeService.addTheme(request))
                .thenReturn(response);

        // when & then
        mvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/themes/1"))
                .andExpect(content().json(responseJson));
    }

    @Test
    @DisplayName("존재하는 테마를 삭제하면 204 를 응답한다.")
    void deleteTheme204() throws Exception {
        // given
        Mockito.when(themeService.deleteTheme(Mockito.anyLong()))
                .thenReturn(1);

        // when & then
        mvc.perform(delete("/themes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하면 404 를 응답한다.")
    void deleteTheme404() throws Exception {
        // given
        final String message = "테마가 존재하지 않습니다.";
        Mockito.when(themeService.deleteTheme(Mockito.anyLong()))
                .thenThrow(new ThemeNotFoundException(message));

        // when & then
        mvc.perform(delete("/themes/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString(message)));
    }

    @Test
    @DisplayName("인기 테마 목록을 조회하면 200 과 테마 리스트를 응답한다.")
    void getPopularThemes() throws Exception {
        // given
        final PopularThemeRequest request = new PopularThemeRequest(7, 10);
        final List<PopularThemeResponse> response = List.of(
                new PopularThemeResponse("Name 1", "Thumbnail 1", "Description 1")
        );
        final String responseJson = objectMapper.writeValueAsString(response);

        Mockito.when(themeService.getPopularThemes(request))
                .thenReturn(response);

        // when & then
        mvc.perform(get("/themes/popular")
                        .queryParam("days", String.valueOf(request.days()))
                        .queryParam("limit", String.valueOf(request.limit())))
                .andExpect(status().isOk())
                .andExpect(content().json(responseJson));
    }
}
