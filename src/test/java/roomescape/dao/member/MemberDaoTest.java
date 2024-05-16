package roomescape.dao.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberInfo;
import roomescape.domain.member.Role;


@JdbcTest
@Import(MemberDao.class)
@Sql(scripts = {"/test_schema.sql"})
class MemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @DisplayName("회원을 추가한다.")
    @Test
    void addMember() {
        // given
        String email = "email@email.com";
        String password = "password";
        String name = "name";

        // when
        Member member = new Member(null, name, email, password, Role.USER);
        MemberInfo inserted = memberDao.insert(member);

        // then
        assertThat(inserted.getName()).isSameAs(member.getName());
        assertThat(inserted.getId()).isEqualTo(1L);
    }

    @DisplayName("모든 회원을 조회한다.")
    @Test
    void findAll() {
        // given
        memberDao.insert(new Member(null, "name1", "email1@email.com", "password1", Role.USER));
        memberDao.insert(new Member(null, "name2", "email2@email.com", "password2", Role.USER));

        // when
        List<MemberInfo> members = memberDao.findAll();

        // then
        assertThat(members).hasSize(2);
        assertThat(members).extracting("name").contains("name1", "name2");
    }

    @DisplayName("이메일로 회원을 조회한다.")
    @Test
    void findByEmail() {
        // given
        MemberInfo inserted = memberDao.insert(
                new Member(null, "name1", "email1@email.com", "password1", Role.USER));

        // when
        MemberInfo found = memberDao.findByEmail("email1@email.com").orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo(inserted.getName());
    }

    @DisplayName("잘못된 이메일을 넣으면 빈 객체를 반환한다.")
    @Test
    void findByInvalidEmail() {
        // given
        MemberInfo inserted = memberDao.insert(
                new Member(null, "name1", "email1@email.com", "password1", Role.USER));

        // when
        String invalidEmail = "email2@email.com";

        // then
        assertThat(memberDao.findByEmail(invalidEmail)).isEmpty();
    }

    @DisplayName("아이디로 회원을 조회한다.")
    @Test
    void findById() {
        // given
        MemberInfo inserted = memberDao.insert(
                new Member(null, "name1", "email1@email.com", "password1", Role.USER));

        // when
        MemberInfo found = memberDao.findById(1L).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo(inserted.getName());
    }

    @DisplayName("잘못된 아이디를 넣으면 빈 객체를 반환한다.")
    @Test
    void findByInvalidId() {
        // given
        MemberInfo inserted = memberDao.insert(
                new Member(null, "name1", "email1@email.com", "password1", Role.USER));

        // when
        Long invalidId = 0L;

        // then
        assertThat(memberDao.findById(invalidId)).isEmpty();
    }

    @DisplayName("이메일과 비밀번호로 회원을 조회한다.")
    @Test
    void isMemberExist() {
        // given
        String email = "email1@email.com";
        String wrongEmail = "wrongemail@email.com";
        String password = "password1";
        String wrongPassword = "wrongpassword";

        memberDao.insert(new Member(null, "name1", email, password, Role.USER));

        // when & then
        assertThat(memberDao.isMemberNotExist(email, password)).isFalse();
        assertThat(memberDao.isMemberNotExist(email, wrongPassword)).isTrue();
        assertThat(memberDao.isMemberNotExist(wrongEmail, password)).isTrue();
    }

    @DisplayName("이메일이 존재하는지 확인한다.")
    @Test
    void isEmailExist() {
        // given
        String email = "email@email.com";
        String password = "password";
        memberDao.insert(new Member(null, "name1", email, password, Role.USER));

        // when & then
        assertThat(memberDao.isEmailExist(email)).isTrue();
    }
}
