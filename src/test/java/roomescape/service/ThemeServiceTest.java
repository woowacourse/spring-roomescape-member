package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ThemeDao;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
class ThemeServiceTest {

    @Autowired
    private ThemeService themeService;

    @Autowired
    private ThemeDao themeDao;

    @Test
    void 테마를_추가한다() {
        // when
        ThemeResponse response = themeService.addTheme(new ThemeRequest("방탈출1", "설명", "https://thumb.com"));

        // then
        assertThat(response.name()).isEqualTo("방탈출1");
    }

    @Test
    void 이미_존재하는_테마명으로_추가하면_예외가_발생한다() {
        // given
        saveTheme("방탈출1", "설명", "https://thumb.com");

        // when & then
        assertThatThrownBy(() -> themeService.addTheme(new ThemeRequest("방탈출1", "설명2", "https://thumb2.com")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 테마입니다.");
    }

    @Test
    void 전체_테마를_조회한다() {
        // given
        saveTheme("방탈출1", "설명1", "https://thumb1.com");
        saveTheme("방탈출2", "설명2", "https://thumb2.com");

        // when
        List<ThemeResponse> responses = themeService.getThemes();

        // then
        assertThat(responses).hasSize(2);
        assertThat(responses).extracting("name").containsExactly("방탈출1", "방탈출2");
    }

    @Test
    void 인기_테마를_조회한다() {
        // given
        saveTheme("공포의 저택", "설명", "https://thumb.com");
        LocalDate endDate = LocalDate.of(2026, 5, 5);

        // when
        List<ThemeResponse> responses = themeService.getPopularThemes(endDate);

        // then
        assertThat(responses).isNotNull();
    }

    @Test
    void 테마를_삭제한다() {
        // given
        Theme saved = saveTheme("방탈출1", "설명", "https://thumb.com");

        // when & then
        assertThatNoException().isThrownBy(() -> themeService.delete(saved.getId()));
    }

    @Test
    void 존재하지_않는_테마를_삭제하면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> themeService.delete(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }

    private Theme saveTheme(String name, String description, String thumbnail) {
        return themeDao.insert(Theme.createWithoutId(name, description, thumbnail));
    }
}
