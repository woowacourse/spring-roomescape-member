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
    @DisplayName("테마 랭킹 목록을 조회할 수 있다")
    void getRankedThemes() throws Exception {
        LocalDate startDate = LocalDate.of(2026, 5, 1);
        LocalDate endDate = LocalDate.of(2026, 5, 31);

        List<Theme> themes = List.of(
                new Theme(1L, "Mystery", "Find the clues", "thumb1"),
                new Theme(2L, "Horror", "Escape the room", "thumb2")
        );

        when(userThemeService.getThemes(eq("reservationCount"), eq("DESC"), eq(startDate), eq(endDate), eq(10L)))
                .thenReturn(themes);

        mockMvc.perform(get("/themes/rank")
                        .param("sort", "reservationCount")
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
    void getRankedThemesWithDefaultSortAndOrder() throws Exception {
        LocalDate startDate = LocalDate.of(2026, 5, 1);
        LocalDate endDate = LocalDate.of(2026, 5, 31);

        when(userThemeService.getThemes(eq("reservationCount"), eq("DESC"), eq(startDate), eq(endDate), eq(10L)))
                .thenReturn(List.of());

        mockMvc.perform(get("/themes/rank")
                        .param("startDate", "2026-05-01")
                        .param("endDate", "2026-05-31")
                        .param("limit", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("기본값이 적용된다")
    void getRankedThemesWithDefaults() throws Exception {
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);

        when(userThemeService.getThemes(eq("reservationCount"), eq("DESC"), eq(weekAgo), eq(today), isNull()))
                .thenReturn(List.of());

        mockMvc.perform(get("/themes/rank")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("전체 테마 목록을 조회할 수 있다")
    void getAllThemes() throws Exception {
        List<Theme> themes = List.of(
                new Theme(1L, "Mystery", "Find the clues", "thumb1"),
                new Theme(2L, "Horror", "Escape the room", "thumb2")
        );

        when(userThemeService.getAllThemes()).thenReturn(themes);

        mockMvc.perform(get("/themes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Mystery"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Horror"));
    }
}
