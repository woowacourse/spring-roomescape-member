package roomescape.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Member;

class MemberTest {

    @DisplayName("식별자는 id이며, id가 같으면 같은 member이다.")
    @Test
    void identify_withEmail() {
        // given
        Long id = 1L;

        // when
        Member member1 = new Member(id, "email1", "password", "멍구");
        Member member2 = new Member(id, "email2", "password2", "멍구2");

        // then
        assertThat(member1).isEqualTo(member2);
    }

}
