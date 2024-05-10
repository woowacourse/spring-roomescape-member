package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    @DisplayName("생성 테스트")
    void create() {
        Name name = new Name("호돌");

        assertThatCode(() -> new Member(name, "email", "password", MemberRole.NORMAL))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("주어진 비밀번호가 올바른지 확인할 수 있다.")
    void isValidPassword() {
        Member member = new Member(new Name("아톰"), "email", "password", MemberRole.NORMAL);

        Assertions.assertAll(
                () -> assertThat(member.isValidPassword("password")).isTrue(),
                () -> assertThat(member.isValidPassword("wrong")).isFalse()
        );
    }
}
