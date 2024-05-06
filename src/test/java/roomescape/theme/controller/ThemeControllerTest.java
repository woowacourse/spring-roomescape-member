package roomescape.theme.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import roomescape.common.DateTimeFormatConfiguration;
import roomescape.reservationtime.controller.ReservationTimeController;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.theme.dto.request.CreateThemeRequest;
import roomescape.theme.dto.response.CreateThemeResponse;
import roomescape.theme.dto.response.FindPopularThemesResponse;
import roomescape.theme.dto.response.FindThemeResponse;
import roomescape.theme.model.Theme;
import roomescape.theme.service.ThemeService;
import roomescape.util.ThemeFixture;

@ActiveProfiles("test")
@WebMvcTest({
        ThemeController.class,
        DateTimeFormatConfiguration.class
})
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ThemeService themeService;

    @Test
    @DisplayName("예약 테마 생성 요청 시 201 상태와 Location 헤더에 생성된 리소스의 위치를 반환한다.")
    void createTheme() throws Exception {
        // given
        CreateThemeRequest createThemeRequest = new CreateThemeRequest("마크", "도망갔다.", "https://abc.com");
        Theme theme = createThemeRequest.toTheme();

        // stub
        Mockito.when(themeService.createTheme(createThemeRequest))
                .thenReturn(CreateThemeResponse.from(theme));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/themes")
                        .content(objectMapper.writeValueAsString(createThemeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        header().stringValues("Location", "/themes/" + theme.getId()),
                        jsonPath("$.id").value(theme.getId()),
                        jsonPath("$.name").value(theme.getName()),
                        jsonPath("$.description").value(theme.getDescription()),
                        jsonPath("$.thumbnail").value(theme.getThumbnail())
                );
    }

    @Test
    @DisplayName("테마 목록 조회 요청 성공 시 200과 해당 정보를 반환한다.")
    void getThemes() throws Exception {
        // given
        List<Theme> themes = ThemeFixture.get(2);
        Theme theme1 = themes.get(0);
        Theme theme2 = themes.get(1);

        // stub
        Mockito.when(themeService.getThemes())
                .thenReturn(List.of(
                        FindThemeResponse.from(theme1),
                        FindThemeResponse.from(theme2)));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/themes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$[0].id").value(theme1.getId()),
                        jsonPath("$[0].name").value(theme1.getName()),
                        jsonPath("$[0].description").value(theme1.getDescription()),
                        jsonPath("$[0].thumbnail").value(theme1.getThumbnail()),

                        jsonPath("$[1].id").value(theme2.getId()),
                        jsonPath("$[1].name").value(theme2.getName()),
                        jsonPath("$[1].description").value(theme2.getDescription()),
                        jsonPath("$[1].thumbnail").value(theme2.getThumbnail()),

                        status().isOk()
                );
    }

    @Test
    @DisplayName("인기 테마 목록 조회 요청 성공 시 200과 해당 정보를 반환한다.")
    void getPopularThemes() throws Exception {
        // given
        List<Theme> themes = ThemeFixture.get(2);
        Theme theme1 = themes.get(0);
        Theme theme2 = themes.get(1);

        // stub
        Mockito.when(themeService.getPopularThemes())
                .thenReturn(List.of(
                        FindPopularThemesResponse.from(theme1),
                        FindPopularThemesResponse.from(theme2)));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/themes/popular")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        jsonPath("$[0].id").value(theme1.getId()),
                        jsonPath("$[0].name").value(theme1.getName()),
                        jsonPath("$[0].description").value(theme1.getDescription()),
                        jsonPath("$[0].thumbnail").value(theme1.getThumbnail()),

                        jsonPath("$[1].id").value(theme2.getId()),
                        jsonPath("$[1].name").value(theme2.getName()),
                        jsonPath("$[1].description").value(theme2.getDescription()),
                        jsonPath("$[1].thumbnail").value(theme2.getThumbnail()),

                        status().isOk()
                );
    }

    @Test
    @DisplayName("테마 삭제 요청 성공 시 204 상태 코드를 반환한다.")
    void deleteTheme() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/themes/10"))
                .andExpect(status().isNoContent());
    }
}
