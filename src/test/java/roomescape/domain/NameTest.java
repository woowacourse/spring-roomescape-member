package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NameTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        assertThatCode(() -> new Name("아톰"))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {" ", "", "\n", "\r"})
    @DisplayName("이름은 공백을 제외한 1글자 이상이어야 한다.")
    void cantCreate(String source) {
        assertThatThrownBy(() -> new Name(source))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이름은 공백을 제외한 1글자 이상이어야 합니다.");
    }
}
