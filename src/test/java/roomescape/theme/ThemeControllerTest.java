package roomescape.theme;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.theme.repository.ThemeRepository;

@WebMvcTest(ThemeController.class)
@Import(ThemeService.class)
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThemeRepository themeRepository;

    @Test
    void 테마_조회() throws Exception {
        Theme horror = new Theme(1L, "공포의 방",
                "심장 약한 사람은 들어오지 마세요.",
                "https://example.com/themes/horror.jpg");
        Theme mystery = new Theme(2L, "미스터리 추리",
                "셜록이 되어 사건을 해결해보세요.",
                "https://example.com/themes/mystery.jpg");

        given(themeRepository.findAll()).willReturn(List.of(horror, mystery));

        mockMvc.perform(get("/api/themes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("공포의 방"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("미스터리 추리"));
    }

    @Test
    void 인기_테마_조회() throws Exception {
        LocalDate fixedNow = LocalDate.of(2026, 5, 11);
        Theme popular = new Theme(5L, "초보자 방",
                "방탈출이 처음이신 분들을 위한 입문 테마.",
                "https://example.com/themes/beginner.jpg");

        try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
            mocked.when(LocalDate::now).thenReturn(fixedNow);

            given(themeRepository.findPopularThemes(any(LocalDate.class), any(LocalDate.class)))
                    .willReturn(List.of(popular));

            mockMvc.perform(get("/api/themes/popularity"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(5))
                    .andExpect(jsonPath("$[0].name").value("초보자 방"));

            verify(themeRepository).findPopularThemes(
                    LocalDate.of(2026, 5, 4),
                    LocalDate.of(2026, 5, 10)
            );
        }
    }
}
