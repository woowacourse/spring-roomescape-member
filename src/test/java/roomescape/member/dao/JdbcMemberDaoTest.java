package roomescape.member.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.domain.Member;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcMemberDaoTest {

    @Autowired
    private MemberDao memberDao;

    @DisplayName("데이터 베이스에 멤버를 추가하고 id 값을 반환한다")
    @Test
    void insertTest() {
        // given
        final Member member = Member.registerUser("라젤", "razel@email.com", "razelpassword");

        // when
        final long result = memberDao.insert(member);

        // then
        assertThat(result).isEqualTo(3L);
    }

    @DisplayName("같은 이메일이 존재하면 true를 반환한다")
    @Test
    void existsByEmailTest() {
        // when
        final boolean result = memberDao.existsByEmail("user@email.com");

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("존재하는 모든 멤버를 찾아 반환한다")
    @Test
    void findAllTest() {
        // given // when
        final List<Member> results = memberDao.findAll();

        // then
        Assertions.assertNotNull(results);
        assertAll(
                () -> assertThat(results).hasSize(2),
                () -> assertThat(results.getFirst().getName()).isEqualTo("어드민"),
                () -> assertThat(results.get(1).getName()).isEqualTo("사용자")
        );
    }

    @DisplayName("id에 해당하는 멤버를 반환한다")
    @Test
    void findByIdTest() {
        // given
        final long id = 1L;

        // when
        final Optional<Member> resultOptional = memberDao.findById(id);

        // then
        assertThat(resultOptional).isPresent();
        assertThat(resultOptional.get().getId()).isEqualTo(id);
    }


    @DisplayName("주어진 이메일을 가진 멤버를 반환한다")
    @Test
    void findByEmailTest() {
        // given
        final String email = "admin@email.com";

        // when
        final Optional<Member> resultOptional = memberDao.findByEmail(email);

        // then
        assertThat(resultOptional).isPresent();
        assertThat(resultOptional.get().getEmail()).isEqualTo(email);
    }


}
