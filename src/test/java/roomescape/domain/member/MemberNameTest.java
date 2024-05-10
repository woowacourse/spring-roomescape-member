package roomescape.domain.member;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MemberNameTest {
    @Test
    void 이름의_길이가_2글자_미만이면_예외가_발생한다() {
        assertThatThrownBy(() -> new MemberName("a"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름 길이는 2글자 이상, 10글자 이하여야 합니다.");
    }

    @Test
    void 이름의_길이가_10글자_초과이면_예외가_발생한다() {
        assertThatThrownBy(() -> new MemberName("qwerasdfghz"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름 길이는 2글자 이상, 10글자 이하여야 합니다.");
    }

    @Test
    void 이름에_한글_영어_외의_문자가_포함되어있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new MemberName("홍길동1"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름이 영어이거나 한글이 아닙니다.");
    }

    @Test
    void 이름이_비어있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new MemberName(null))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}
