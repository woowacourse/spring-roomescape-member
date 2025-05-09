package roomescape.member.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberNameTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"aaaaaaaaaaaaaaa"})
    @DisplayName("맴버 이름 예외 테스트")
    void name_exception_test(String name) {
        Assertions.assertThatThrownBy(() -> new MemberName(name))
                .isInstanceOf(IllegalArgumentException.class);
    }
}