package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.Member;
import roomescape.persistence.dao.JdbcMemberDao;
import roomescape.persistence.dao.MemberDao;
import roomescape.presentation.dto.MemberResponse;

@JdbcTest
class MemberServiceTest {

    private final MemberService memberService;
    private final MemberDao memberDao;

    @Autowired
    public MemberServiceTest(final JdbcTemplate jdbcTemplate) {
        this.memberDao = new JdbcMemberDao(jdbcTemplate);
        this.memberService = new MemberService(memberDao);
    }

    @Test
    @DisplayName("모든 사용자를 조회한다")
    void findAll() {
        // given
        final Member member1 = new Member("name1", "role1", "email1", "password1");
        memberDao.insert(member1);
        final Member member2 = new Member("name2", "role2", "email2", "password2");
        memberDao.insert(member2);

        // when
        final List<MemberResponse> memberResponses = memberService.findAll();

        // then
        assertThat(memberResponses).hasSize(2);
    }
}
