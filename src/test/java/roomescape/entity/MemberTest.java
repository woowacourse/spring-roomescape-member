package roomescape.entity;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.impl.NameContainsNumberException;
import roomescape.exception.impl.OverMaxNameLengthException;

public class MemberTest {

    @Test
    @DisplayName("멤버의 이름에 숫자가 포함되면 안된다.")
    void ShouldThrowExceptionWhenNameIncludeNumber() {
        // given
        String name = "lemon123";
        // when
        // then
        Assertions.assertThatThrownBy(() -> Member.beforeMemberSave(name, "suwon@com", "123"))
                .isInstanceOf(NameContainsNumberException.class);
    }

    @Test
    @DisplayName("멤버의 이름은 10글자를 넘어가면 안된다.")
    void ShouldThrowExceptionWhenNamelengthOver10() {
        // given
        String name = "lemonlemonlemon";
        // when
        // then
        Assertions.assertThatThrownBy(() -> Member.beforeMemberSave(name, "suwon@com", "123"))
                .isInstanceOf(OverMaxNameLengthException.class);
    }
}
