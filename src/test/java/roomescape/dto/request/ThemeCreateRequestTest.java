package roomescape.dto.request;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ThemeCreateRequestTest {

    @DisplayName("이름이 null이면 예외가 발생한다.")
    @Test
    void validThrowsIfNameIsNull() {

        // given
        final String name = null;

        // when & then
        assertThatThrownBy(() -> new ThemeCreateRequest(name, "test", "test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }

    @DisplayName("이름이 빈 값이면 예외가 발생한다.")
    @Test
    void validThrowsIfNameIsBlank() {

        // given
        final String name = "";

        // when & then
        assertThatThrownBy(() -> new ThemeCreateRequest(name, "test", "test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }

    @DisplayName("설명이 null이면 예외가 발생한다.")
    @Test
    void validThrowsIfDescriptionIsNull() {

        // given
        final String description = null;

        // when & then
        assertThatThrownBy(() -> new ThemeCreateRequest("test", description, "test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }

    @DisplayName("설명이 빈 값이면 예외가 발생한다.")
    @Test
    void validThrowsIfDescriptionIsBlank() {

        // given
        final String description = "";

        // when & then
        assertThatThrownBy(() -> new ThemeCreateRequest("test", description, "test"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }

    @DisplayName("썸네일이 null이면 예외가 발생한다.")
    @Test
    void validThrowsIfThumbnailIsNull() {

        // given
        final String thumbnail = null;

        // when & then
        assertThatThrownBy(() -> new ThemeCreateRequest("test", "test", thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }

    @DisplayName("썸네일이 빈 값이면 예외가 발생한다.")
    @Test
    void validThrowsIfThumbnailIsBlank() {

        // given
        final String thumbnail = "";

        // when & then
        assertThatThrownBy(() -> new ThemeCreateRequest("test", "test", thumbnail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 값으로 예약할 수 없습니다.");
    }
}
