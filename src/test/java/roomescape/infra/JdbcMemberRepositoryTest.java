package roomescape.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.entity.Member;
import roomescape.domain.entity.Role;

@JdbcTest
@Import(JdbcMemberRepository.class)
class JdbcMemberRepositoryTest {

    @Autowired
    private JdbcMemberRepository sut;

    @DisplayName("회원을 저장한다")
    @Test
    void save() {
        // given
        var member = new Member(null, "홍길동", "hong@example.com", "pw123", Role.USER);

        // when
        var saved = sut.save(member);

        // then
        assertSoftly(soft -> {
            soft.assertThat(saved.getId()).isNotNull();
            soft.assertThat(saved.getName()).isEqualTo("홍길동");
            soft.assertThat(saved.getEmail()).isEqualTo("hong@example.com");
            soft.assertThat(saved.getPassword()).isEqualTo("pw123");
            soft.assertThat(saved.getRole()).isEqualTo(Role.USER);
        });
    }

    @DisplayName("이메일로 회원이 존재하는지 확인할 수 있다")
    @Test
    void existsByEmail() {
        // given
        var member = sut.save(new Member(null, "홍길동", "hong@example.com", "pw123", Role.USER));

        // when
        boolean existsByEmail = sut.existsByEmail(member.getEmail());

        // then
        assertThat(existsByEmail).isTrue();
    }

    @DisplayName("이메일로 회원을 조회한다")
    @Test
    void findByEmail() {
        // given
        var saved = sut.save(new Member(null, "홍길동", "hong@example.com", "pw123", Role.USER));

        // when
        var found = sut.findByEmail(saved.getEmail()).get();

        // then
        assertThat(found).isEqualTo(saved);
    }

    @DisplayName("ID로 회원을 조회한다")
    @Test
    void findById() {
        // given
        var saved = sut.save(new Member(null, "홍길동", "hong@example.com", "pw123", Role.USER));

        // when
        var found = sut.findById(saved.getId()).get();

        // then
        assertThat(found).isEqualTo(saved);
    }

    @DisplayName("모든 회원을 조회한다")
    @Test
    void findAll() {
        // given
        sut.save(new Member(null, "홍길동", "hong@example.com", "pw123", Role.USER));
        sut.save(new Member(null, "머피", "song@example.com", "pw456", Role.USER));

        // when
        List<Member> all = sut.findAll();

        // then
        assertThat(all).hasSize(2);
    }
}
