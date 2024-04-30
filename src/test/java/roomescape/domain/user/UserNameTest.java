package roomescape.domain.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class UserNameTest {
    @Test
    void 이름의_길이가_2글자_미만이면_예외가_발생한다() {
        assertThatThrownBy(() -> new UserName("a"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름은 2글자 이상이어야 합니다.");
    }

    @Test
    void 이름에_한글_영어_외의_문자가_포함되어있으면_예외가_발생한다() {
        assertThatThrownBy(() -> new UserName("홍길동1"))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이름은 한글, 영어만 가능합니다.");
    }
}
