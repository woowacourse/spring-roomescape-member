package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.ThemeRequestDTO;
import roomescape.dto.ThemeResponseDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    ThemeService themeService;

    @DisplayName("테마를 생성한다")
    @Test
    void ThemeRequestDTO를_받아_ThemeResponseDTO를_리턴한다() {
        ThemeRequestDTO themeRequestDTO = new ThemeRequestDTO("sample theme", "샘플 테마입니다", "example.com");

        ThemeResponseDTO addedTheme = themeService.addTheme(themeRequestDTO);

        assertThat(addedTheme)
                .usingRecursiveComparison()
                .ignoringFields("id", "runningTime")
                .isEqualTo(themeRequestDTO);
    }

    @DisplayName("모든 테마를 조회한다")
    @Test
    void 존재하는_모든_테마의_ThemeResponseDTO가_담긴_리스트를_리턴한다() {
        // given
        ThemeResponseDTO addedSampleATheme = themeService.addTheme(
                new ThemeRequestDTO("sample a theme", "샘플 테마입니다", "example.com")
        );
        ThemeResponseDTO addedSampleBTheme = themeService.addTheme(
                new ThemeRequestDTO("sample b theme", "샘플 테마입니다", "example.com")
        );

        // when
        List<ThemeResponseDTO> allThemes = themeService.findAllThemes();

        // then
        assertThat(allThemes)
                .hasSize(2)
                .containsExactlyInAnyOrder(addedSampleATheme, addedSampleBTheme);
    }

    @DisplayName("특정 테마를 조회한다")
    @Test
    void 테마의_id로_테마를_조회한다() {
        ThemeResponseDTO addedTheme = themeService.addTheme(
                new ThemeRequestDTO("sample theme", "샘플 테마입니다", "example.com")
        );

        ThemeResponseDTO foundTheme = themeService.findById(addedTheme.id());

        assertThat(foundTheme).isEqualTo(addedTheme);
    }

    @DisplayName("인기 테마를 조회한다")
    @Sql("/data.sql")
    @Test
    void 인기_테마를_조회한다() {
        List<ThemeResponseDTO> foundPopularThemes = themeService.findPopularThemes();

        assertThat(foundPopularThemes)
                .as("인기 테마는 상위 10개 항목을 리턴해야 합니다")
                .hasSize(10)
                .map(ThemeResponseDTO::id)
                .as("포함되어야 할 인기 테마가 없거나, 순서가 잘못되었습니다")
                .containsExactlyInAnyOrder(1L, 2L, 3L, 6L, 5L, 4L, 8L, 7L, 10L, 9L);
    }

    @DisplayName("테마를 삭제한다")
    @Test
    void 테마의_id로_테마를_삭제한다() {
        ThemeResponseDTO addedTheme = themeService.addTheme(
                new ThemeRequestDTO("sample theme", "샘플 테마입니다", "example.com")
        );

        themeService.deleteTheme(addedTheme.id());

        assertThat(themeService.findAllThemes()).isEmpty();
    }
}