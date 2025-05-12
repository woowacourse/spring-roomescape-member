package roomescape.member.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@JdbcTest
@Import(JdbcMemberDao.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@Sql(scripts = "/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class JdbcMemberDaoTest {

    @Autowired
    JdbcMemberDao memberDao;

    @DisplayName("이메일과 비밀번호로 멤버를 찾는다.")
    @Test
    void findByEmailAndPassword() {

        // given
        String email = "test@test.com";
        String password = "qwer1234!";
        memberDao.create(Member.createWithoutId("test", email, password, Role.USER));

        // when
        Optional<Member> memberOptional = memberDao.findByEmailAndPassword(email, password);

        // then
        assertThat(memberOptional).isPresent();
    }

    @DisplayName("멤버를 추가한다.")
    @Test
    void create() {

        // when
        Member member = Member.createWithoutId("test", "test@test.com", "qwer1234!", Role.USER);

        // then
        assertThatCode(() -> memberDao.create(member))
                .doesNotThrowAnyException();

    }

    @DisplayName("이메일로 멤버를 찾는다.")
    @Test
    void findByEmail() {

        // given
        String email = "test@test.com";
        String password = "qwer1234!";
        memberDao.create(Member.createWithoutId("test", email, password, Role.USER));

        // when
        Optional<Member> memberOptional = memberDao.findByEmail(email);

        // then
        assertThat(memberOptional).isPresent();
    }

    @DisplayName("전체 멤버를 찾는다.")
    @Test
    void findAll() {

        // given
        memberDao.create(Member.createWithoutId("test1", "test1@test.com", "qwer1234!", Role.USER));
        memberDao.create(Member.createWithoutId("test2", "test2@test.com", "qwer1234!", Role.USER));
        memberDao.create(Member.createWithoutId("test3", "test3@test.com", "qwer1234!", Role.USER));

        // when
        List<Member> members = memberDao.findAll();

        // then
        assertThat(members).hasSize(3);
    }
}