package roomescape.theme.service;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.EntityNotFoundException;
import roomescape.theme.exception.ThemeDuplicateException;
import roomescape.fake.ThemeFakeRepository;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.repository.ThemeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ThemeServiceTest {

    private final ThemeRepository themeRepository = new ThemeFakeRepository();
    private final ThemeService themeService = new ThemeService(themeRepository);

    @Test
    @DisplayName("조회된 테마 엔티티를 DTO에 매핑해 반환한다.")
    void test_readThemes() {
        // when
        List<ThemeResponse> themeResponses = themeService.readThemes();

        // then
        assertThat(themeResponses.size()).isEqualTo(1);
        assertThat(themeResponses.getFirst().name()).isEqualTo("레벨2 탈출");
    }

    @Test
    @DisplayName("엔티티를 저장한 후, DTO로 반환한다.")
    void test_createTheme() {
        //given
        String given = "테스트";
        ThemeRequest request = new ThemeRequest(given, "설명", "썸네일");
        //when
        ThemeResponse actual = themeService.createTheme(request);
        //then
        AssertionsForClassTypes.assertThat(actual.name()).isEqualTo(given);
    }

    @Test
    @DisplayName("저장소에 없는 값을 삭제하려할 경우, 예외가 발생한다.")
    void test_deleteThemeById() {
        assertThatThrownBy(() -> themeService.deleteThemeById(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("예약 시간 생성 시, 중복된 테마명일 경우 예외가 발생한다.")
    void error_createThemeIfDuplicationThemeName() {
        //given
        ThemeRequest request = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        //when&then
        assertThatThrownBy(() -> themeService.createTheme(request))
                .isInstanceOf(ThemeDuplicateException.class)
                .hasMessageContaining("중복된 테마명이 존재합니다.");
    }
}
