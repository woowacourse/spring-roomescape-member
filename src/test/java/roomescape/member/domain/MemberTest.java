package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {
    @DisplayName("이름이 비어있을 때 예외를 던진다.")
    @Test
    void validateThemeTest_whenNameIsNull() {
        assertThatThrownBy(() -> new Member(null, "abc@abc.com", "1234"))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("이메일이 비어있을 때 예외를 던진다.")
    @Test
    void validateThemeTest_whenDescriptionIsNull() {
        assertThatThrownBy(() -> new Member("커찬", null, "1234"))
                .isInstanceOf(NullPointerException.class);
    }

    @DisplayName("썸네일이 비어있을 때 예외를 던진다.")
    @Test
    void validateThemeTest_whenThumbnailIsNull() {
        assertThatThrownBy(() -> new Member("커찬", "abc@abc.com", null))
                .isInstanceOf(NullPointerException.class);
    }
}
