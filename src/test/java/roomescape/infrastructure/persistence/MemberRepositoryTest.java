package roomescape.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.support.IntegrationTestSupport;

/*
 * 테스트 데이터베이스 멤버 초기 데이터
 * {ID=1, NAME="어드민", EMAIL="admin@test.com"}
 */
class MemberRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private MemberRepository target;

    @Test
    @DisplayName("이메일로 사용자를 조회할 수 있다.")
    void findByEmail() {
        Optional<Member> findMember = target.findByEmail("admin@test.com");

        assertThat(findMember)
                .map(Member::getName)
                .map(Name::value)
                .isNotEmpty()
                .get()
                .isEqualTo("어드민");
    }

    @Test
    @DisplayName("이메일로 사용자를 조회할 수 있다.")
    void notFound() {
        Optional<Member> findMember = target.findByEmail("admin@test.com");

        assertThat(findMember)
                .map(Member::getName)
                .map(Name::value)
                .isNotEmpty()
                .get()
                .isEqualTo("어드민");
    }
}
