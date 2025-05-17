package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.Theme;
import roomescape.exception.ThemeNotFoundException;
import roomescape.fake.FakeThemeDao;
import roomescape.presentation.dto.theme.ThemeRequest;
import roomescape.presentation.dto.theme.ThemeResponse;

class ThemeServiceTest {

    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        themeService = new ThemeService(new FakeThemeDao());
    }

    @DisplayName("테마를 조회한다.")
    @Test
    void find() {
        // given
        themeService.create(new ThemeRequest("테마", "소개", "썸네일"));

        final Long id = 1L;
        final Theme expected = Theme.createWithId(1L, "테마", "소개", "썸네일");

        // when & then
        assertThat(themeService.find(id))
                .isEqualTo(expected);
    }

    @DisplayName("조회하려는 테마 id가 없다면 예외가 발생한다.")
    @Test
    void findOrThrowIfIdNotExists() {
        // given
        final Long id = 1L;

        // when & then
        assertThatThrownBy(() -> themeService.find(id))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @DisplayName("테마를 저장한다.")
    @Test
    void create() {
        // given
        final ThemeRequest themeRequest = new ThemeRequest("테마", "소개", "썸네일");
        final ThemeResponse expected = new ThemeResponse(1L, "테마", "소개", "썸네일");

        // when & then
        assertThat(themeService.create(themeRequest))
                .isEqualTo(expected);
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void findAll() {
        // given
        themeService.create(new ThemeRequest("테마1", "설명1", "썸네일1"));
        themeService.create(new ThemeRequest("테마2", "설명2", "썸네일2"));

        // when & then
        assertThat(themeService.findAll()).containsExactly(
                new ThemeResponse(1L, "테마1", "설명1", "썸네일1"),
                new ThemeResponse(2L, "테마2", "설명2", "썸네일2")
        );
    }

    @DisplayName("테마를 삭제한다.")
    @Test
    void remove() {
        // given
        themeService.create(new ThemeRequest("테마1", "설명1", "썸네일1"));
        themeService.remove(1L);

        // when & then
        assertThat(themeService.findAll()).isEmpty();
    }

    @DisplayName("삭제하려는 테마 id가 없다면 예외가 발생한다.")
    @Test
    void removeOrThrowIfIdNotExists() {
        // given & when & then
        assertThatThrownBy(() -> themeService.remove(1L))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @DisplayName("인기 테마를 조회한다.")
    @Test
    void findPopularThemes() {
        // given
        themeService.create(new ThemeRequest("테마1", "설명1", "썸네일1"));
        themeService.create(new ThemeRequest("테마2", "설명2", "썸네일2"));
        themeService.create(new ThemeRequest("테마3", "설명3", "썸네일3"));
        themeService.create(new ThemeRequest("테마4", "설명4", "썸네일4"));

        // when
        List<ThemeResponse> popularThemes = themeService.findPopularThemes();

        // then
        assertThat(popularThemes).isNotNull();
        assertThat(popularThemes.size()).isLessThanOrEqualTo(10);
        assertThat(popularThemes).extracting("name")
                .containsExactly("테마3", "테마1", "테마2", "테마4");
    }
}
