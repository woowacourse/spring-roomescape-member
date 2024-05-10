package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.exception.InvalidDomainObjectException;
import roomescape.domain.theme.Theme;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ThemeTest {
    @DisplayName("테마를 생성한다")
    @Test
    void when_createTheme_then_created() {
        assertThatCode(() -> new Theme("테마", "테마 설명", "https://thumbnail.jpg"))
                .doesNotThrowAnyException();
    }

    @DisplayName("20자보다 작은 이름의 테마를 생성할 수 있다")
    @Test
    void when_createThemeWithShortName_then_created() {
        assertThatCode(() -> new Theme("테".repeat(20), "테마 설명", "https://thumbnail.jpg"))
                .doesNotThrowAnyException();
    }

    @DisplayName("20자가 넘는 이름의 테마를 생성하면, 예외가 발생한다")
    @Test
    void when_createThemeWithLongName_then_exception() {
        assertThatThrownBy(() -> new Theme("123456789012345678901", "테마 설명", "https://thumbnail.jpg"))
                .isInstanceOf(InvalidDomainObjectException.class);
    }

    @DisplayName("2자보다 작은 이름의 테마를 생성할 수 있다")
    @Test
    void when_createThemeWithLongName_then_created() {
        assertThatCode(() -> new Theme("테마", "테마 설명", "https://thumbnail.jpg"))
                .doesNotThrowAnyException();
    }

    @DisplayName("2자보다 작은 이름의 테마를 생성하면, 예외가 발생한다")
    @Test
    void when_createThemeWithShortName_then_exception() {
        assertThatThrownBy(() -> new Theme("테", "테마 설명", "https://thumbnail.jpg"))
                .isInstanceOf(InvalidDomainObjectException.class);
    }

    @DisplayName("255자보다 작은 설명의 테마를 생성할 수 있다")
    @Test
    void when_createThemeWithShortDescription_then_created() {
        assertThatCode(() -> new Theme("테마", "설".repeat(255), "https://thumbnail.jpg"))
                .doesNotThrowAnyException();
    }

    @DisplayName("255자가 넘는 설명의 테마를 생성하면, 예외가 발생한다")
    @Test
    void when_createThemeWithLongDescription_then_exception() {
        assertThatThrownBy(() -> new Theme("테마", "설".repeat(256), "https://thumbnail.jpg"))
                .isInstanceOf(InvalidDomainObjectException.class);
    }

    @DisplayName("255자가 넘는 썸네일 URL의 테마를 생성하면, 예외가 발생한다")
    @Test
    void when_createThemeWithLongThumbnail_then_exception() {
        assertThatThrownBy(() -> new Theme("테마", "테마 설명", "https://thumbnail.jpg".repeat(50)))
                .isInstanceOf(InvalidDomainObjectException.class);
    }

    @DisplayName("사진 형식이 아닌 썸네일 URL의 테마를 생성하면, 예외가 발생한다")
    @Test
    void when_createThemeWithInvalidThumbnail_then_exception() {
        assertThatThrownBy(() -> new Theme("테마", "테마 설명", "thumbnail.jpg"))
                .isInstanceOf(InvalidDomainObjectException.class);
    }
}
