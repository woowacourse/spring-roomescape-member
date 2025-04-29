package roomescape.model;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserNameTest {

    @Test
    @DisplayName("한글 이름 2~5자, 영문 이름 2~30자만 가능합니다.")
    void test1() {
        assertThatThrownBy(() -> new UserName("놉")).hasMessage("Invalid name format");
        assertThatThrownBy(() -> new UserName("x")).hasMessage("Invalid name format");

        assertThatCode(() -> new UserName("가능한")).doesNotThrowAnyException();
        assertThatCode(() -> new UserName("possible")).doesNotThrowAnyException();
    }

}
