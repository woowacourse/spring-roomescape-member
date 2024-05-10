package roomescape.member.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.util.MemberFixture;

class MemberTest {

    @Test
    @DisplayName("해당하는 문자열이 유저의 비밀번호와 다른 경우, 참을 반환한다.")
    void hasNotSamePassword() {
        Member member = new Member(null, "몰리", MemberRole.USER, "asdf@asdf.com", "pass");
        assertTrue(member.hasNotSamePassword("word"));
    }

    @Test
    @DisplayName("해당하는 문자열이 유저의 비밀번호와 같은 경우, 거짓을 반환한다.")
    void hasNotSamePassword_WhenSamePassword() {
        String samePassword = "pass";
        Member member = new Member(null, "몰리", MemberRole.USER, "asdf@asdf.com", samePassword);
        assertFalse(member.hasNotSamePassword(samePassword));
    }

    @Test
    @DisplayName("해당하는 값이 객체의 이메일 값과 동일한 경우, 참을 반환한다.")
    void isSameEmail() {
        String sameEmail = "hi@asdf.com";
        Member member = MemberFixture.getOne(sameEmail);
        assertTrue(member.isSameEmail(sameEmail));
    }

    @Test
    @DisplayName("해당하는 값이 객체의 이메일 값과 동일하지 않은 경우, 거짓을 반환한다.")
    void isNotSameEmail() {
        String email = "hi@asdf.com";
        Member member = MemberFixture.getOne(email);
        assertFalse(member.isSameEmail("!!!" + email));
    }
}
