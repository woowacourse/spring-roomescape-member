package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestInstance(Lifecycle.PER_METHOD)
class ThemeServiceTest {

    private static final int DEFAULT_COUNT = 5;

    @Autowired
    private ThemeService themeService;

    @Test
    @DisplayName("저장된 모든 테마를 조회한다.")
    void findAll() {
        assertThat(themeService.findAll()).hasSize(DEFAULT_COUNT);
    }

    @Test
    @DisplayName("지난 한 주간 가장 많이 예약된 10개의 인기 테마를 조회한다.")
    void getLastWeekTop10() {
        LocalDate currentDate = LocalDate.parse("2024-05-07");
        assertThat(themeService.getLastWeekTop10(currentDate)).containsExactly(
                new Theme(1, "Theme1", "Description for Theme1", "thumbnail1"),
                new Theme(2, "Theme2", "Description for Theme2", "thumbnail2"),
                new Theme(3, "Theme3", "Description for Theme3", "thumbnail3")
        );
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
