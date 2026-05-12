package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.theme.domain.Theme;

@SpringBootTest
@Transactional
class ThemeServiceTest {
    private static final String DEFAULT_DESCRIPTION = "테마 설명";
    private static final String DEFAULT_THUMBNAIL_URL = "테마 썸네일";

    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("등록된 테마가 여러개이면 조회 시 등록된 갯수만큼 반환한다.")
    void findThemes() {
        // given
        List<Theme> themes = List.of(
                Theme.create("테마1", "테마1 설명", "테마1 썸네일"),
                Theme.create("테마2", "테마2 설명", "테마2 썸네일"),
                Theme.create("테마3", "테마3 설명", "테마3 썸네일")
        );
        saveAll(themes);

        // when
        List<Theme> actual = themeService.findThemes();

        // then
        assertThat(actual).hasSize(themes.size());
    }

    @Test
    @DisplayName("등록된 테마와 조회되는 테마의 모든 필드가 일치한다.")
    void findTheme() {
        // given
        Theme savedTheme = themeService.register("테마1", "테마1 설명", "테마1 썸네일");

        // when
        Theme actual = themeService.findTheme(savedTheme.id());

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(savedTheme);
    }

    @Test
    @DisplayName("등록되지 않은 테마 조회 시 예외가 발생한다.")
    void findTheme_unregistered() {
        // given
        Long unregisteredId = Long.MIN_VALUE;

        // when & then
        assertThatThrownBy(() -> themeService.findTheme(unregisteredId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 테마가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("활성화된 테마 목록을 가나다순으로 조회한다.")
    void findActiveThemes() {
        // given
        String name1 = "다테마";
        String name2 = "나테마";
        String name3 = "가테마";

        List<Theme> themes = saveAll(generateActiveThemesByName(List.of(name1, name2, name3)));
        themes.sort(Comparator.comparing(Theme::name));

        // when
        List<Theme> actual = themeService.findActiveThemes();

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(themes);
    }

    @Test
    @DisplayName("테마를 1개 등록하면 테마 데이터 수가 1 증가한다.")
    void register() {
        // given
        String name = "테마1";
        String description = "테마1 설명";
        String thumbnail = "테마1 썸네일";

        // when
        themeService.register(name, description, thumbnail);

        // then
        assertThat(themeService.findThemes())
                .hasSize(1);
    }

    @Test
    @DisplayName("등록한 테마와 다시 조회한 테마의 모든 필드가 일치한다.")
    void register_theme_fields_match() {
        // given
        String name = "테마1";
        String description = "테마1 설명";
        String thumbnail = "테마1 썸네일";

        // when
        Theme registeredTheme = themeService.register(name, description, thumbnail);

        // then
        assertThat(registeredTheme)
                .usingRecursiveComparison()
                .isEqualTo(themeService.findTheme(registeredTheme.id()));
    }

    @Test
    @DisplayName("테마를 활성화한다.")
    void updateStatus_active() {
        // given
        Theme savedTheme = themeService.register("테마1", "테마1 설명", "테마1 썸네일");

        // when
        themeService.updateStatus(savedTheme.id(), true);

        // then
        assertThat(themeService.findTheme(savedTheme.id()).isActive())
                .isTrue();
    }

    @Test
    @DisplayName("테마를 비활성화한다.")
    void updateStatus_deactivate() {
        // given
        Theme savedTheme = themeService.register("테마1", "테마1 설명", "테마1 썸네일");
        savedTheme.updateStatus(true);

        // when
        themeService.updateStatus(savedTheme.id(), false);

        // then
        assertThat(themeService.findTheme(savedTheme.id()).isActive())
                .isFalse();
    }

    private List<Theme> saveAll(List<Theme> themes) {
        List<Theme> savedThemes = new ArrayList<>();
        for (Theme theme : themes) {
            Theme savedTheme = themeService.register(theme.name(), theme.description(), theme.thumbnailUrl());
            themeService.updateStatus(savedTheme.id(), theme.isActive()); // 활성화 상태 반영
            savedThemes.add(themeService.findTheme(savedTheme.id()));
        }
        return savedThemes;
    }

        private List<Theme> generateActiveThemesByName(List<String> names) {
        List<Theme> themes = new ArrayList<>();
        for (String name : names) {
            Theme theme = Theme.create(name, DEFAULT_DESCRIPTION, DEFAULT_THUMBNAIL_URL);
            theme.updateStatus(true);
            themes.add(theme);
        }
        return themes;
    }
}
