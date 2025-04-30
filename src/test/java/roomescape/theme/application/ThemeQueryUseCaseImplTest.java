package roomescape.theme.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;
import roomescape.theme.domain.ThemeDescription;
import roomescape.theme.domain.ThemeName;
import roomescape.theme.domain.ThemeRepository;
import roomescape.theme.domain.ThemeThumbnail;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ThemeQueryUseCaseImplTest {

    @Autowired
    private ThemeQueryUseCaseImpl themeQueryUseCase;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("테마를 모두 조회할 수 있다")
    void getAll() {
        // given
        final String name1 = "시소";
        final String description1 = "공포 방탈출 대표 테마";
        final String url1 = "https://www.naver.com";

        final Theme saved1 = themeRepository.save(Theme.withoutId(
                ThemeName.from(name1),
                ThemeDescription.from(description1),
                ThemeThumbnail.from(url1)));

        final String name2 = "강산";
        final String description2 = "유머 방탈출 대표 테마";
        final String url2 = "https://www.daum.com";

        final Theme saved2 = themeRepository.save(Theme.withoutId(
                ThemeName.from(name2),
                ThemeDescription.from(description2),
                ThemeThumbnail.from(url2)));

        // when
        final List<Theme> themes = themeQueryUseCase.getAll();

        // then
        assertThat(themes).hasSize(2);
        assertThat(themes).contains(saved1, saved2);
    }
}
