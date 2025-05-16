package roomescape.theme.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.exception.custom.InUseException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.dto.CreateThemeServiceRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeServiceTest {

    @Autowired
    ThemeService themeService;

    @Test
    @DisplayName("테마를 추가한다")
    void addThemeTest() {
        // given
        final CreateThemeServiceRequest creation = new CreateThemeServiceRequest("test", "description",
                "thumbnail");
        // when // then
        assertThatCode(() -> themeService.addTheme(creation))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("같은 테마가 존재하면 예외를 던진다")
    void addThemeTest_WhenThemeAlreadyExists() {
        // given
        final CreateThemeServiceRequest creation = new CreateThemeServiceRequest("test", "description",
                "thumbnail");
        themeService.addTheme(creation);

        // when // then
        assertThatThrownBy(() -> themeService.addTheme(creation))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessageContaining("이미 존재하는 테마입니다");
    }

    @Test
    @DisplayName("존재하는 모든 테마를 조회한다")
    void findAllThemesTest() {
        // given // when
        final List<Theme> allThemes = themeService.findAllThemes();

        // then
        assertThat(allThemes).hasSize(3);
    }

    @Test
    @DisplayName("테마를 삭제한다")
    void deleteThemeTest() {
        // given
        themeService.addTheme(new CreateThemeServiceRequest("test", "description", "thumbnail"));
        final long deleteId = 3L;

        // when // then
        assertThatCode(() -> themeService.deleteTheme(deleteId))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("사용중인 테마를 삭제하는 경우 예외를 던진다")
    void deleteThemeTest_WhenThemeIsUsedInReservation() {
        // given
        final long deleteId = 1L;

        // when // then
        assertThatThrownBy(() -> themeService.deleteTheme(deleteId))
                .isInstanceOf(InUseException.class)
                .hasMessageContaining("사용 중인 테마입니다");
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하는 경우 예외를 던진다")
    void deleteThemeTest_WhenThemeDoesNotExist() {
        // given
        final long deleteId = 1000L;

        // when // then
        assertThatThrownBy(() -> themeService.deleteTheme(deleteId))
                .isInstanceOf(NotExistedValueException.class)
                .hasMessageContaining("존재하지 않는 테마입니다");
    }
}
