package roomescape.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRole;
import roomescape.member.domain.Password;

@JdbcTest
@Import(JdbcMemberRepository.class)
class JdbcMemberRepositoryTest {

    @Autowired
    private JdbcMemberRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 새로운_멤버를_저장() {
        // given
        Member member = Member.builder()
                .name("테스터")
                .email("test@example.com")
                .password(Password.createForMember("secret"))
                .role(MemberRole.MEMBER)
                .build();

        // when
        repository.save(member);
        Optional<Member> found = repository.findByEmail("test@example.com");

        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(found).isPresent();
            Member result = found.get();
            soft.assertThat(result.getId()).isNotNull();
            soft.assertThat(result.getName()).isEqualTo("테스터");
            soft.assertThat(result.getEmail()).isEqualTo("test@example.com");
            soft.assertThat(result.getPassword()).isEqualTo("secret");
            soft.assertThat(result.getRole()).isEqualTo(MemberRole.MEMBER);
        });
    }

    @Test
    void 저장된_멤버를_id로_조회() {
        // given
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "A", "a@a.com", "pw", "ADMIN"
        );
        Long id = jdbcTemplate.queryForObject("SELECT id FROM member WHERE email = ?", Long.class, "a@a.com");

        // when
        Optional<Member> found = repository.findById(id);

        // then
        SoftAssertions.assertSoftly(soft -> {
            soft.assertThat(found).isPresent();
            Member result = found.get();
            soft.assertThat(result.getId()).isEqualTo(id);
            soft.assertThat(result.getName()).isEqualTo("A");
            soft.assertThat(result.getEmail()).isEqualTo("a@a.com");
            soft.assertThat(result.getPassword()).isEqualTo("pw");
            soft.assertThat(result.getRole()).isEqualTo(MemberRole.ADMIN);
        });
    }

    @Test
    void 저장된_모든_멤버_리스트_조회() {
        // given
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "X", "x@x.com", "px", "MEMBER"
        );
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "Y", "y@y.com", "py", "ADMIN"
        );

        // when
        List<Member> members = repository.findAll();

        // then - 기존 data.sql에 2개 존재
        SoftAssertions.assertSoftly(soft -> {
            assertThat(members).hasSize(4);
            assertThat(members).extracting(Member::getName).containsExactlyInAnyOrder("운영진", "홍길동", "X", "Y");
            assertThat(members).extracting(Member::getRole)
                    .containsExactlyInAnyOrder(MemberRole.ADMIN, MemberRole.MEMBER, MemberRole.MEMBER,
                            MemberRole.ADMIN);
        });
    }

    @Test
    void 저장된_멤버를_이메일로_조회() {
        // given
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) VALUES (?, ?, ?, ?)",
                "B", "b@b.com", "pb", "MEMBER"
        );

        // when
        Optional<Member> found = repository.findByEmail("b@b.com");

        // then
        SoftAssertions.assertSoftly(soft -> {
            assertThat(found).isPresent();
            Member result = found.get();
            assertThat(result.getEmail()).isEqualTo("b@b.com");
            assertThat(result.getName()).isEqualTo("B");
        });
    }
}

