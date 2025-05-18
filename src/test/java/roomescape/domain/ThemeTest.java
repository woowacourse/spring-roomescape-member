package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ThemeTest {

    @Test
    @DisplayName("name이 null이면 예외를 던진다")
    void name_not_null() {
        assertThatThrownBy(() -> {
            new Theme(
                null,
                "탈출 설명",
                "abc.jpg");
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("description이 null이면 예외를 던진다")
    void description_not_null() {
        assertThatThrownBy(() -> {
            new Theme(
                "탈출",
                null,
                "abc.jpg");
        }).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("thumbnail이 null이면 예외를 던진다")
    void thumbnail_not_null() {
        assertThatThrownBy(() -> {
            new Theme(
                "탈출",
                "탈출 설명",
                null);
        }).isInstanceOf(NullPointerException.class);
    }
}
