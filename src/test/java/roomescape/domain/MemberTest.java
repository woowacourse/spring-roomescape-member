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
        Email email = new Email("email");
        Password password = new Password("******");

        assertThatCode(() -> new Member(name, email, password, MemberRole.NORMAL))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("주어진 비밀번호가 올바른지 확인할 수 있다.")
    void isValidPassword() {
        Member member = new Member(
                new Name("아톰"),
                new Email("email"),
                new Password("password"), MemberRole.NORMAL
        );

        Password answer = new Password("password");
        Password wrongpassword = new Password("wrongPw");
        Assertions.assertAll(
                () -> assertThat(member.isValidPassword(answer)).isTrue(),
                () -> assertThat(member.isValidPassword(wrongpassword)).isFalse()
        );
    }

    @Test
    @DisplayName("어드민 계정인지 확인할 수 있다.")
    void isAdmin() {
        Member member = new Member(
                new Name("아톰"),
                new Email("email"),
                new Password("******"), MemberRole.ADMIN
        );

        assertThat(member.isAdmin()).isTrue();
    }

    @Test
    @DisplayName("어드민 계정이 아닌지 확인할 수 있다.")
    void isNotAdmin() {
        Member member = new Member(
                new Name("아톰"),
                new Email("email"),
                new Password("******"),
                MemberRole.NORMAL
        );

        assertThat(member.isAdmin()).isFalse();
    }
}
