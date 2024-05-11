package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MemberNameTest {
    @ParameterizedTest
    @NullAndEmptySource
    void 사용자명이_비어있으면_예외가_발생한다(String name) {
        assertThatThrownBy(() -> new MemberName(name))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자명이 비어있습니다.");
    }

    @Test
    void 사용자명에_영어_한글_외의_문자가_포함되어있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new MemberName("홍길동1"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자명은 영어, 한글만 가능합니다.");
    }

    @Test
    void 사용자명이_2글자_미만이면_예외가_발생한다() {
        assertThatThrownBy(() -> new MemberName("a"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자명은 2글자 이상, 10글자 이하여야 합니다.");
    }

    @Test
    void 사용자명이_10글자_초과이면_예외가_발생한다() {
        assertThatThrownBy(() -> new MemberName("qwerasdfghz"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("사용자명은 2글자 이상, 10글자 이하여야 합니다.");
    }
}
