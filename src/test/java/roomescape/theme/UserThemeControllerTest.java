package roomescape.theme;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserThemeController.class)
class UserThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserThemeService userThemeService;

    @Test
    @DisplayName("테마 목록을 조회할 수 있다")
    void getThemes() throws Exception {
        LocalDate startDate = LocalDate.of(2026, 5, 1);
        LocalDate endDate = LocalDate.of(2026, 5, 31);

        List<ThemeResponse> response = List.of(
                new ThemeResponse(1L, "Mystery", "Find the clues", "thumb1"),
                new ThemeResponse(2L, "Horror", "Escape the room", "thumb2")
        );

        when(userThemeService.findThemes(eq("id"), eq("DESC"), eq(startDate), eq(endDate), eq(10L)))
                .thenReturn(response);

        mockMvc.perform(get("/themes")
                        .param("sort", "id")
                        .param("order", "DESC")
                        .param("startDate", "2026-05-01")
                        .param("endDate", "2026-05-31")
                        .param("limit", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Mystery"))
                .andExpect(jsonPath("$[0].description").value("Find the clues"))
                .andExpect(jsonPath("$[0].thumbnail").value("thumb1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Horror"))
                .andExpect(jsonPath("$[1].description").value("Escape the room"))
                .andExpect(jsonPath("$[1].thumbnail").value("thumb2"));
    }

    @Test
    @DisplayName("정렬 기본값이 적용된다")
    void getThemesWithDefaultSortAndOrder() throws Exception {
        LocalDate startDate = LocalDate.of(2026, 5, 1);
        LocalDate endDate = LocalDate.of(2026, 5, 31);

        when(userThemeService.findThemes(eq("id"), eq("DESC"), eq(startDate), eq(endDate), eq(10L)))
                .thenReturn(List.of());

        mockMvc.perform(get("/themes")
                        .param("startDate", "2026-05-01")
                        .param("endDate", "2026-05-31")
                        .param("limit", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("기본값이 적용된다")
    void getThemesWithDefaults() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);

        when(userThemeService.findThemes(eq("id"), eq("DESC"), eq(weekAgo), eq(today), isNull()))
                .thenReturn(List.of());

        mockMvc.perform(get("/themes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
