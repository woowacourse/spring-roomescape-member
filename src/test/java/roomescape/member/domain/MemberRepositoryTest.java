package roomescape.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.RepositoryTest;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.TestFixture.USER_MIA;

class MemberRepositoryTest extends RepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자를 저장한다.")
    void save() {
        // given
        Member member = USER_MIA();

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertThat(savedMember.getId()).isNotNull();
    }
}
