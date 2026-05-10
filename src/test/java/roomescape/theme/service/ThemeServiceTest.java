package roomescape.theme.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fixture.ThemeFixture;
import roomescape.fixture.fake.FakeThemeRepository;
import roomescape.theme.application.dto.PopularThemeQueryResult;
import roomescape.theme.application.dto.ThemeQueryResult;
import roomescape.theme.application.exception.ThemeException;
import roomescape.theme.application.query.PopularTheme;
import roomescape.theme.application.service.ThemeService;

public class ThemeServiceTest {

    private FakeThemeRepository themeRepository;
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        themeRepository = new FakeThemeRepository();
        themeService = new ThemeService(themeRepository, themeRepository);
    }

    @DisplayName("테마의 정상 추가를 테스트합니다.")
    @Test
    void save_theme_successfully() {
        ThemeQueryResult result = themeService.save(ThemeFixture.horrorThemeCreateCommand());

        assertThat(result).isEqualTo(ThemeFixture.horrorThemeQueryResult(1L));
    }

    @DisplayName("중복된 테마 추가 시 예외 발생을 테스트합니다.")
    @Test
    void save_duplicated_theme_exception() {
        themeService.save(ThemeFixture.horrorThemeCreateCommand());

        assertThatThrownBy(() -> themeService.save(ThemeFixture.horrorThemeCreateCommand()))
                .isInstanceOf(ThemeException.class)
                .hasMessage("이름과 설명이 같은 테마가 이미 존재합니다.");
    }

    @DisplayName("테마의 삭제를 테스트합니다.")
    @Test
    void delete_theme() {
        themeService.save(ThemeFixture.horrorThemeCreateCommand());

        assertThat(themeService.delete(1L)).isEqualTo(1);
    }

    @DisplayName("테마 조회를 테스트합니다.")
    @Test
    void find_theme() {
        themeService.save(ThemeFixture.horrorThemeCreateCommand());

        assertThat(themeService.findById(1L)).isEqualTo(ThemeFixture.horrorThemeQueryResult(1L));
    }

    @DisplayName("존재하지 않는 테마 조회 시 예외 발생을 테스트합니다.")
    @Test
    void theme_not_exists() {
        assertThatThrownBy(() -> themeService.findById(100L))
                .isInstanceOf(ThemeException.class)
                .hasMessage("존재하지 않는 테마 입니다.");
    }

    @DisplayName("테마의 전체 조회를 테스트합니다.")
    @Test
    void find_all_themes() {
        themeService.save(ThemeFixture.themeCreateCommand(1));
        themeService.save(ThemeFixture.themeCreateCommand(2));
        themeService.save(ThemeFixture.themeCreateCommand(3));

        List<ThemeQueryResult> themes = themeService.findAll();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(themes.size()).isEqualTo(3);
            softly.assertThat(themes).containsExactly(
                    ThemeFixture.themeQueryResult(1, 1L),
                    ThemeFixture.themeQueryResult(2, 2L),
                    ThemeFixture.themeQueryResult(3, 3L)
            );
        });
    }

    @DisplayName("인기 테마 조회를 테스트합니다.")
    @Test
    void find_popular_themes() {
        themeRepository.savePopularTheme(new PopularTheme(
                1L,
                "인기 테마",
                "인기 테마 설명",
                "https://example.com/popular.png",
                10
        ));

        LocalDate today = LocalDate.of(2026, 5, 6);
        List<PopularThemeQueryResult> responses = themeService.findPopularThemes(today);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(responses).containsExactly(
                    new PopularThemeQueryResult(
                            1L,
                            "인기 테마",
                            "인기 테마 설명",
                            "https://example.com/popular.png",
                            10
                    )
            );
        });
    }
}