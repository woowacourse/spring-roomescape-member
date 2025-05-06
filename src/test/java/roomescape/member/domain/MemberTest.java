package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("식별자는 id이며, id가 같으면 같은 회원이다.")
    @Test
    void identify_whenId() {
        // given
        Long id = 1L;

        // when
        Member member1 = new Member(id, "email1", "password", "멍구", Role.USER);
        Member member2 = new Member(id, "email2", "password2", "멍구2", Role.USER);

        // then
        assertThat(member1).isEqualTo(member2);
    }

    @DisplayName("id가 null이면 다른 회원과 동일 취급되지 않는다.")
    @Test
    void noSameMember_whenNullId() {
        // when
        Member member1 = new Member(null, "email1", "password", "멍구", Role.USER);
        Member member2 = new Member(null, "email1", "password", "멍구", Role.USER);

        // then
        assertThat(member1).isNotEqualTo(member2);
    }

}
