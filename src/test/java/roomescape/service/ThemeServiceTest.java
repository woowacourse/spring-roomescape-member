package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import roomescape.BaseTest;
import roomescape.domain.Theme;

class ThemeServiceTest extends BaseTest {

    private static final int DEFAULT_COUNT = 5;

    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("저장된 모든 테마를 조회한다.")
    void findAll() {
        assertThat(themeService.findAll()).hasSize(DEFAULT_COUNT);
    }

    @Test
    @DisplayName("테마를 추가한다.")
    void create() {
        assertThemeCountIsEqualTo(DEFAULT_COUNT);
        Theme newTheme = new Theme(0, "Woowa", "Description for Woowa", "woowa-thumbnail");
        themeService.create(newTheme);
        assertThemeCountIsEqualTo(6);
    }

    @Test
    @DisplayName("특정 id의 테마를 삭제한다.")
    void delete() {
        assertThemeCountIsEqualTo(DEFAULT_COUNT);
        themeService.delete(5);
        assertThemeCountIsEqualTo(4);
    }

    @Test
    void existsById() {
        assertThat(themeService.existsById(1)).isTrue();
    }

    @Test
    void existsByName() {
        assertThat(themeService.existsByName("Theme1")).isTrue();
    }

    void assertThemeCountIsEqualTo(int count) {
        assertThat(themeService.findAll()).hasSize(count);
    }
}
