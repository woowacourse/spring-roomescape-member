package roomescape.domain.auth.entity;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.common.exception.InvalidArgumentException;

class NameTest {

    @DisplayName("이름 테스트")
    @ValueSource(strings = {"꾹", "포스티", "aaaaabbbbbcccccdddddabc25"})
    @ParameterizedTest
    void getNameTest1(final String name) {
        // given
        // when
        // then
        assertThatCode(() -> new Name(name)).doesNotThrowAnyException();
    }

    @DisplayName("공백이거나 25자 이상이면 예외를 반환한다.")
    @NullAndEmptySource
    @ParameterizedTest
    @ValueSource(strings = {"  ", "aaaaaaaaaabbbbbbbbbbcccc26"})
    void getName_throwsException(final String name) {
        // given
        // when & then
        assertThatThrownBy(() -> {
            new Name(name);
        }).isInstanceOf(InvalidArgumentException.class);
    }

}
