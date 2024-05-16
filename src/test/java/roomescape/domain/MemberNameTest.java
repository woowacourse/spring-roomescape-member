package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.member.MemberName;
import roomescape.exception.EmptyParameterException;
import roomescape.exception.ParameterException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("멤버 이름")
class MemberNameTest {
    @DisplayName("멤버 이름이 공백인 경우 예외가 발생한다.")
    @ValueSource(strings = {"", " ", "    ", "\n", "\r", "\t"})
    @ParameterizedTest
    void validateNonBlankName(String blankName) {
        assertThatThrownBy(() -> new MemberName(blankName))
                .isInstanceOf(EmptyParameterException.class)
                .hasMessage("회원 이름이 비어 있습니다.");
    }

    @DisplayName("멤버 이름이 50자를 초과하는 경우 예외가 발생한다.")
    @Test
    void validateNameLength() {
        String name = "A".repeat(51);
        assertThatThrownBy(() -> new MemberName(name))
                .isInstanceOf(ParameterException.class)
                .hasMessage("회원 이름은 50자 이내여야 합니다.");
    }
}
