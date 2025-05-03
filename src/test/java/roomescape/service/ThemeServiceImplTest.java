package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.dto.ThemeRequestDto;
import roomescape.dto.ThemeResponseDto;

@SpringBootTest
public class ThemeServiceImplTest {

    @Autowired
    @Qualifier("testThemeService")
    private ThemeService themeService;

    @DisplayName("테마의 정보를 받아와 저장하고, ID를 설정할 수 있어야 한다.")
    @Test
    void given_theme_dto_then_set_id_and_save() {
        ThemeRequestDto themeRequestDto = new ThemeRequestDto(
            "테마1", "공포테마입니다", "http://aaa");
        ThemeResponseDto themeResponseDto = themeService.saveTheme(themeRequestDto);
        assertThat(themeResponseDto.themeId()).isNotNull();
        assertThat(themeResponseDto.name()).isEqualTo("테마1");
        assertThat(themeResponseDto.description()).isEqualTo("공포테마입니다");
        assertThat(themeResponseDto.thumbnail()).isEqualTo("http://aaa");
    }

    @DisplayName("존재하지 않는 테마를 삭제할 경우, 예외가 발생해야 한다.")
    @Test
    void when_delete_invalid_theme_then_throw_exception() {
        assertThatThrownBy(() -> themeService.deleteTheme(9999999999999L))
            .isInstanceOf(IllegalArgumentException.class);
    }

    //TODO : getAllThemeOfRanks 메서드 테스트 코드 작성
}


