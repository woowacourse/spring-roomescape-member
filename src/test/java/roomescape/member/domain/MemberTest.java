package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.exception.MemberFieldRequiredException;

class MemberTest {

    @DisplayName("회원은 이름 없이 생성할 수 없다")
    @Test
    void memberWithoutNameThrowsException() {
        assertThatThrownBy(() ->
                Member.createWithoutId(" ", "test@example.com", "password123"))
                .isInstanceOf(MemberFieldRequiredException.class);
    }

    @DisplayName("회원은 이메일 없이 생성할 수 없다")
    @Test
    void memberWithoutEmailThrowsException() {
        assertThatThrownBy(() ->
                Member.createWithoutId("John", " ", "password123"))
                .isInstanceOf(MemberFieldRequiredException.class);
    }

    @DisplayName("회원은 비밀번호 없이 생성할 수 없다")
    @Test
    void memberWithoutPasswordThrowsException() {
        assertThatThrownBy(() ->
                Member.createWithoutId("John", "test@example.com", " "))
                .isInstanceOf(MemberFieldRequiredException.class);
    }
}
