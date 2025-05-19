package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;

@JdbcTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    DataSource dataSource;
    JdbcTemplate template;
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        template = new JdbcTemplate(dataSource);
        memberRepository = new MemberRepositoryImpl(dataSource);
        template.execute(
                "insert into member (name, email, password, role) values ('제프리','jeffrey@gamil.com','1234!@#$','USER')");
    }

    @AfterEach
    void tearDown() {
        template.execute("DELETE FROM member");
        template.execute("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
    }

    @Test
    void findById() {
        //when
        Member member = memberRepository.findById(1L).get();

        //then
        assertEqualMember(member, 1L, "제프리", "jeffrey@gamil.com", "1234!@#$", MemberRole.USER);
    }

    @Test
    void findAll() {
        //when
        List<Member> members = memberRepository.findAll();

        //then
        Member member = members.getFirst();
        assertThat(members).hasSize(1);
        assertEqualMember(member, 1L, "제프리", "jeffrey@gamil.com", "1234!@#$", MemberRole.USER);
    }

    @Test
    void save() {
        //given
        Member member = new Member("윌슨", "wilson@gamil.com", "1234!@#$", MemberRole.USER);

        //when
        Member saved = memberRepository.save(member);
        Member firstMember = memberRepository.findById(1L).get();
        Member secondMember = memberRepository.findById(2L).get();

        //then
        assertThat(saved.getId()).isEqualTo(2L);
        assertEqualMember(firstMember, 1L, "제프리", "jeffrey@gamil.com", "1234!@#$", MemberRole.USER);
        assertEqualMember(secondMember, 2L, "윌슨", "wilson@gamil.com", "1234!@#$", MemberRole.USER);
    }

    @Test
    void deleteById() {
        //when
        boolean result = memberRepository.deleteById(1L);

        //then
        assertThat(result).isTrue();
        assertThat(memberRepository.findAll()).isEmpty();
    }

    private void assertEqualMember(final Member member,
                                   final long id,
                                   final String name,
                                   final String email,
                                   final String password,
                                   final MemberRole role) {

        assertThat(member.getId()).isEqualTo(id);
        assertThat(member.getName()).isEqualTo(name);
        assertThat(member.getEmail()).isEqualTo(email);
        assertThat(member.getPassword()).isEqualTo(password);
        assertThat(member.getRole()).isEqualTo(role);
    }
}
