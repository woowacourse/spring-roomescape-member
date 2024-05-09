package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import roomescape.BasicAcceptanceTest;
import roomescape.TestFixtures;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;

@Sql(value = "/setMember.sql")
class H2MemberRepositoryTest extends BasicAcceptanceTest {
    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("멤버를 DB에 저장한다")
    @Test
    void save() {
        memberRepository.save(TestFixtures.MEMBER_3);

        List<Member> expected = List.of(TestFixtures.MEMBER_1, TestFixtures.MEMBER_2, TestFixtures.MEMBER_3);

        assertThat(memberRepository.findAll()).isEqualTo(expected);
    }

    @DisplayName("해당 email과 password를 가진 멤버가 존재하면 true를 반환한다")
    @Test
    void checkExistMember() {
        assertThat(memberRepository.checkExistMember("email1", "qq1")).isTrue();
    }

    @DisplayName("해당 email과 password를 가진 멤버가 없으면 false를 반환한다")
    @Test
    void checkNotExistMember() {
        assertThat(memberRepository.checkExistMember("email2", "qq2")).isFalse();
    }

    @DisplayName("해당 email의 멤버를 반환한다")
    @Test
    void findByEmail() {
        Member member = memberRepository.findByEmail("email1");

        assertThat(member).isEqualTo(TestFixtures.MEMBER_2);
    }

    @DisplayName("해당 id의 멤버를 반환한다")
    @Test
    void findById() {
        assertThat(memberRepository.findById(1L).orElseThrow()).isEqualTo(TestFixtures.MEMBER_1);
    }

    @DisplayName("DB에 저장된 전체 멤버를 반환한다")
    @Test
    void findAll() {
        List<Member> members = List.of(TestFixtures.MEMBER_1, TestFixtures.MEMBER_2);

        assertThat(memberRepository.findAll()).isEqualTo(members);
    }
}
