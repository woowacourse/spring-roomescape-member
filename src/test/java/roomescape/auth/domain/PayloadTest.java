package roomescape.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

class PayloadTest {

    @Test
    @DisplayName("Member로부터 Payload를 생성할 수 있다")
    void test1() {
        // given
        Member member = new Member(1L, "미미", "email", "password", Role.MEMBER);
        Payload expectedPayload = new Payload(1L, Role.MEMBER);

        // when
        Payload payload = Payload.from(member);

        // then
        assertThat(payload).isEqualTo(expectedPayload);
    }


    @Test
    @DisplayName("문자열 표현으로부터 Payload를 생성할 수 있다")
    void test2() {
        // given
        String memberIdExpression = "2";
        String roleExpression = "ADMIN";
        Payload expectedPayload = new Payload(2L, Role.ADMIN);

        // when
        Payload payload = Payload.from(memberIdExpression, roleExpression);

        // then
        assertThat(payload).isEqualTo(expectedPayload);
    }

    @Test
    @DisplayName("Payload 생성 시 memberId가 null이면 예외가 발생한다")
    void test3() {
        assertThatThrownBy(() -> new Payload(null, Role.MEMBER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("memberId가 빈 값이어서는 안 됩니다");
    }

    @Test
    @DisplayName("Payload 생성 시 role이 null이면 예외가 발생한다")
    void test4() {
        assertThatThrownBy(() -> new Payload(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("role이 빈 값이어서는 안 됩니다");
    }

    @Test
    @DisplayName("문자열 memberId가 숫자가 아니면 예외가 발생한다")
    void test5() {
        assertThatThrownBy(() -> Payload.from("abc", "MEMBER"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("memberId가 숫자 형식이 아닙니다");
    }

    @Test
    @DisplayName("getMemberIdExpression()은 문자열로 된 memberId를 반환한다")
    void test6() {
        Payload payload = new Payload(123L, Role.ADMIN);

        assertThat(payload.getMemberIdExpression()).isEqualTo("123");
    }

    @Test
    @DisplayName("getRoleExpression()은 Role의 expression을 반환한다")
    void test7() {
        Payload payload = new Payload(1L, Role.MEMBER);

        assertThat(payload.getRoleExpression()).isEqualTo("member");
    }

    @Test
    @DisplayName("isAuthorizedFor()는 권한 검사를 수행한다")
    void test8() {
        Payload adminPayload = new Payload(1L, Role.ADMIN);
        Payload memberPayload = new Payload(2L, Role.MEMBER);

        assertThat(adminPayload.isAuthorizedFor(Role.ADMIN)).isTrue();
        assertThat(memberPayload.isAuthorizedFor(Role.MEMBER)).isTrue();
    }
}
