package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.ThemeResponse;
import roomescape.fake.ThemeFakeRepository;
import roomescape.repository.ThemeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ThemeServiceTest {

    private final ThemeRepository themeRepository = new ThemeFakeRepository();
    private final ThemeService themeService = new ThemeService(themeRepository);

    @Test
    @DisplayName("조회된 테마 엔티티를 DTO에 매핑해 반환한다.")
    void test_readAllTheme() {
        // when
        List<ThemeResponse> themeResponses = themeService.readAllTheme();

        // then
        assertThat(themeResponses.size()).isEqualTo(1);
        assertThat(themeResponses.getFirst().name()).isEqualTo("레벨2 탈출");
    }
}
