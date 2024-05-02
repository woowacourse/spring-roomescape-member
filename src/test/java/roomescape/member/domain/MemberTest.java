package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {
    @DisplayName("동일한 id는 같은 사용자다.")
    @Test
    void equals() {
        //given
        long id1 = 1;
        String name1 = "name1";
        String name2 = "name2";

        //when
        Member member1 = new Member(id1, name1);
        Member member2 = new Member(id1, name2);

        //then
        assertThat(member1).isEqualTo(member2);
    }
}
