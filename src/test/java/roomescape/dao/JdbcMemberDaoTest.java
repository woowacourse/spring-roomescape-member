package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.dao.MemberDao;
import roomescape.domain.member.model.Member;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest
public class JdbcMemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    @DisplayName("Member 객체가 주어졌을 때, 저장할 수 있어야 한다.")
    void can_member_save() {
        Member member = Member.createUser("jenson", "email@email.com", "aaa");
        memberDao.save(member);
        assertThat(memberDao.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("멤버 ID로 멤버를 조회할 수 있어야 한다..")
    void can_get_member_id() {
        Member member = Member.createUser("jenson", "email@email.com", "aaa");
        long savedId = memberDao.save(member);
        member.setId(savedId);

        Optional<Member> findMember = memberDao.findById(savedId);
        assertThat(findMember.isPresent()).isTrue();
    }

    @Test
    @DisplayName("이메일로 멤버를 조회할 수 있어야 한다.")
    void can_get_member_email() {
        Member member = Member.createUser("jenson", "email@email.com", "aaa");
        long savedId = memberDao.save(member);
        member.setId(savedId);

        Optional<Member> findMember = memberDao.findByEmail("email@email.com");
        assertThat(findMember.isPresent()).isTrue();
    }

    @Test
    @DisplayName("이메일과 비밀번호로 멤버를 조회할 수 있어야 한다.")
    void can_get_member_email_and_password() {
        Member member = Member.createUser("jenson", "email@email.com", "aaa");
        long savedId = memberDao.save(member);
        member.setId(savedId);
        Optional<Member> findMember = memberDao.findByEmailAndPassword("email@email.com", "aaa");
        assertThat(findMember.isPresent()).isTrue();
    }
}
