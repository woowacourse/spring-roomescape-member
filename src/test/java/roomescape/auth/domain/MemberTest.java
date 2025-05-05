package roomescape.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("식별자는 email이며, email이 같으면 같은 member이다.")
    @Test
    void identify_withEmail() {
        // given
        String email = "test@email.com";

        // when
        Member member1 = new Member(email, "password", "멍구");
        Member member2 = new Member(email, "password2", "멍구2");

        // then
        assertThat(member1).isEqualTo(member2);
    }

}
