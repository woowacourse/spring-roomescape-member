package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.fixture.ThemeFixture;
import roomescape.theme.application.dto.ThemeResult;
import roomescape.theme.application.exception.ThemeException;
import roomescape.theme.application.service.ThemeCommandService;
import roomescape.theme.infra.JdbcThemeRepository;

@JdbcTest
@Import({ThemeCommandService.class, JdbcThemeRepository.class})
public class ThemeCommandServiceTest {

    @Autowired
    private ThemeCommandService themeCommandService;

    @DisplayName("테마의 정상 추가를 테스트합니다.")
    @Test
    void save_theme_successfully() {
        ThemeResult result = themeCommandService.save(ThemeFixture.horrorThemeCreateCommand());

        assertThat(result.id()).isPositive();
        assertThat(result).isEqualTo(ThemeFixture.horrorThemeQueryResult(result.id()));
    }

    @DisplayName("중복된 테마 추가 시 예외 발생을 테스트합니다.")
    @Test
    void save_duplicated_theme_exception() {
        themeCommandService.save(ThemeFixture.horrorThemeCreateCommand());

        assertThatThrownBy(() -> themeCommandService.save(ThemeFixture.horrorThemeCreateCommand()))
                .isInstanceOf(ThemeException.class)
                .hasMessage("이름과 설명이 같은 테마가 이미 존재합니다.");
    }

    @DisplayName("테마의 삭제를 테스트합니다.")
    @Test
    void delete_theme() {
        ThemeResult result = themeCommandService.save(ThemeFixture.horrorThemeCreateCommand());

        assertThat(themeCommandService.delete(result.id())).isEqualTo(1);
    }
}
