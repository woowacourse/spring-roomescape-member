package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.Member;
import roomescape.domain.Name;
import java.util.Optional;

class MemberJdbcDaoTest extends DaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("사용자를 저장한다.")
    void saveMember() {
        // given
        final Member member = new Member(new Name("냥인"), "nyangin@email.com", "1234");

        // when
        final Member savedMember = memberDao.save(member);

        // then
        assertThat(savedMember.getId()).isNotNull();
    }
    @Test
    @DisplayName("Id에 해당하는 사용자를 조회한다.")
    void findMemberByEmail() {
        // given
        final Member savedMember = memberDao.save(new Member(new Name("냥인"), "nyangin@email.com", "1234"));

        // when
        final Optional<Member> member = memberDao.findById(savedMember.getId());

        // then
        assertThat(member).isPresent();
    }
}
