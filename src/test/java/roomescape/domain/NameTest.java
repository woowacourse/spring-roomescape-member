package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {

    @DisplayName("이름이 비어있으면 name 생성 시 예외가 발생합니다.")
    @Test
    void should_throw_NPE_when_name_is_blank() {
        assertThatThrownBy(() -> new Name(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("이름은 비어있을 수 없습니다.");
    }
}
