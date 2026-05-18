package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ThemeTest {

    @DisplayName("이름이 비어 있는 테마는 생성할 수 없다")
    @ParameterizedTest(name = "이름이 [{0}]이면 생성할 수 없다.")
    @NullSource
    @ValueSource(strings = {"", " ", "  "})
    void 이름이_비어_있으면_IllegalArgumentException_예외를_던진다(String emptyName) {
        assertThatThrownBy(() -> Theme.withoutId(emptyName, "설명", "이미지"))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("설명이 비어 있는 테마는 생성할 수 없다")
    @ParameterizedTest(name = "설명이 [{0}]이면 생성할 수 없다.")
    @NullSource
    @ValueSource(strings = {"", " ", "  "})
    void 설명이_비어_있으면_IllegalArgumentException_예외를_던진다(String emptyDescription) {
        assertThatThrownBy(() -> Theme.withoutId("이름", emptyDescription, "이미지"))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미지 URL이 비어 있는 테마는 생성할 수 없다")
    @ParameterizedTest(name = "이미지가 [{0}]이면 생성할 수 없다.")
    @NullSource
    @ValueSource(strings = {"", " ", "  "})
    void 이미지가_비어_있으면_IllegalArgumentException_예외를_던진다(String emptyImage) {
        assertThatThrownBy(() -> Theme.withoutId("이름", "설명", emptyImage))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
