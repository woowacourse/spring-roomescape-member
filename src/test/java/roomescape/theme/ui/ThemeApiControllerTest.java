package roomescape.theme.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.auth.application.AuthService;
import roomescape.theme.application.ThemeService;
import roomescape.theme.dto.PopularThemeResponse;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;

@WebMvcTest(ThemeApiController.class)
class ThemeApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThemeService themeService;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("테마를 등록할 수 있다")
    void test1() throws Exception {
        // given
        ThemeRequest request = new ThemeRequest("테마1", "테마 설명", "테마 썸네일");
        ThemeResponse response = new ThemeResponse(1L, "테마1", "테마 설명", "테마 썸네일");

        when(themeService.add(any(ThemeRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/themes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("테마1"))
                .andExpect(jsonPath("$.description").value("테마 설명"))
                .andExpect(jsonPath("$.thumbnail").value("테마 썸네일"));
    }

    @Test
    @DisplayName("전체 테마 목록을 조회할 수 있다")
    void test2() throws Exception {
        // given
        ThemeResponse response = new ThemeResponse(1L, "테마1", "테마 설명", "테마 썸네일");

        when(themeService.findAll()).thenReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("테마1"))
                .andExpect(jsonPath("$[0].description").value("테마 설명"))
                .andExpect(jsonPath("$[0].thumbnail").value("테마 썸네일"));
    }

    @Test
    @DisplayName("지난주 가장 많이 예약된 상위 10개 테마를 조회할 수 있다")
    void test3() throws Exception {
        // given
        PopularThemeResponse response = new PopularThemeResponse("테마1", "테마 설명", "테마 썸네일");

        when(themeService.findTop10MostReservedLastWeek()).thenReturn(List.of(response));

        // when & then
        mockMvc.perform(get("/themes/popular/weekly"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("테마1"));
    }

    @Test
    @DisplayName("테마를 삭제할 수 있다")
    void test4() throws Exception {
        // given
        doNothing().when(themeService).deleteById(1L);

        // when & then
        mockMvc.perform(delete("/themes/1"))
                .andExpect(status().isNoContent());
    }
}
