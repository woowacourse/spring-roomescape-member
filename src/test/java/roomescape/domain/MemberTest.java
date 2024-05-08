package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("유저의 비밀번호가 일치하는지 확인한다.")
    void isMatchPasswordTest() {
        String password = "password";

        Member member = new Member(new Name("userA"), new Email("admin@email.com"), password);

        assertThat(member.isMatchPassword(password)).isTrue();
    }

    @Test
    @DisplayName("id를 조회하면 email value를 가져온다.")
    void getIdTest() {
        String email = "admin@email.com";

        Member member = new Member(new Name("userA"), new Email(email), "password");

        assertThat(member.getId()).isEqualTo(email);
    }

}
