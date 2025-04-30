package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.controller.dto.ThemeRequest;
import roomescape.controller.dto.ThemeResponse;
import roomescape.fake.FakeThemeDao;
import roomescape.service.reservation.Theme;

class ThemeServiceTest {

    FakeThemeDao fakeThemeDao = new FakeThemeDao();
    ThemeService themeService = new ThemeService(fakeThemeDao);

    @DisplayName("저장할 테마 이름이 중복될 경우 예외가 발생한다.")
    @Test
    void testValidateNameDuplication() {
        // given
        ThemeRequest request1 = new ThemeRequest("woooteco", "우테코를 탈출하라", "https://www.woowacourse.io/");
        ThemeRequest request2 = new ThemeRequest("woooteco", "우테코에서 살아남기", "https://www.woowacourse2.io/");
        themeService.createTheme(request1);
        // when
        // then
        assertThatThrownBy(() -> themeService.createTheme(request2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 이름의 테마는 이미 존재합니다.");
    }

    @DisplayName("테마를 저장할 수 있다.")
    @Test
    void testCreate() {
        // given
        ThemeRequest request = new ThemeRequest("woooteco", "우테코를 탈출하라", "https://www.woowacourse.io/");
        // when
        ThemeResponse result = themeService.createTheme(request);
        // then
        Theme saved = fakeThemeDao.findById(result.id());
        assertAll(
                () -> assertThat(result.id()).isEqualTo(1L),
                () -> assertThat(result.name()).isEqualTo(request.name()),
                () -> assertThat(result.description()).isEqualTo(request.description()),
                () -> assertThat(result.thumbnail()).isEqualTo(request.thumbnail()),
                () -> assertThat(saved.getName()).isEqualTo(request.name()),
                () -> assertThat(saved.getDescription()).isEqualTo(request.description()),
                () -> assertThat(saved.getThumbnail()).isEqualTo(request.thumbnail())
        );
    }
}
