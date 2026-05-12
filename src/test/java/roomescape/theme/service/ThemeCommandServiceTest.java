package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.fixture.ThemeFixture;
import roomescape.global.exception.NotFoundException;
import roomescape.theme.application.dto.ThemeResult;
import roomescape.global.exception.RoomEscapeException;
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
                .isInstanceOf(RoomEscapeException.class)
                .hasMessage("이름과 설명이 같은 테마가 이미 존재합니다.");
    }

    @DisplayName("테마의 삭제를 테스트합니다.")
    @Test
    void delete_theme() {
        ThemeResult result = themeCommandService.save(ThemeFixture.horrorThemeCreateCommand());

        assertThatNoException().isThrownBy(() -> themeCommandService.delete(result.id()));
    }

    @DisplayName("삭제할 테마가 없을 시 예외 발생을 테스트합니다.")
    @Test
    void fail_to_delete_theme() {
        assertThatThrownBy(() -> themeCommandService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 테마입니다.");
    }
}
