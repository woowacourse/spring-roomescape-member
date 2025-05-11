package roomescape.model;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.model.user.Name;

class MemberNameTest {

    @Test
    @DisplayName("한글 이름 2~5자, 영문 이름 2~30자만 가능합니다.")
    void test1() {
        assertThatThrownBy(
                () -> new Name("ㅁㄴㅇ")
        ).isInstanceOf(IllegalArgumentException.class);

        assertThatCode(
                () -> new Name("가능한")
        ).doesNotThrowAnyException();
        assertThatCode(
                () -> new Name("possible")
        ).doesNotThrowAnyException();
    }

}
