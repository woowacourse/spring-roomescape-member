package roomescape.repository;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;
import roomescape.domain.MemberRoleType;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(JdbcMemberRepository.class)
@ActiveProfiles("test")
@Sql(scripts = "/schema.sql")
@Sql(scripts = "/reservation-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class JdbcMemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 데이터를 저장한다")
    void insert() {
        //given
        Member member = new Member("new", "new@email.com", "1234", MemberRoleType.MEMBER);

        //when
        long actual = memberRepository.insert(member);

        //then
        assertThat(actual).isEqualTo(3L);
    }

    @ParameterizedTest
    @CsvSource(value = {"MEMBER,1", "ADMIN,1"})
    @DisplayName("권한을 기반으로 멤버 데이터를 조회한다")
    void findAllByRole(MemberRoleType role, int expectedSize) {
        //when
        List<Member> actual = memberRepository.findAllByRole(role);

        //then
        assertThat(actual).hasSize(expectedSize);
    }

    @Test
    @DisplayName("식별자를 기반으로 멤버 데이터를 조회한다")
    void findById() {
        //given
        long id = 1L;

        //when
        Optional<Member> actual = memberRepository.findById(id);

        //then
        assertThat(actual).isPresent();
    }

    @Test
    @DisplayName("존재하지 않는 식별자를 기반으로 멤버 데이터를 조회하면 empty이다")
    void findByNotExistedId() {
        //given
        long id = 1000L;

        //when
        Optional<Member> actual = memberRepository.findById(id);

        //then
        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("email 과 password를 기반으로 멤버 데이터를 조회한다")
    void findByEmailAndPassword() {
        //given
        String email = "member@email.com";
        String password = "1234";

        //when
        Optional<Member> actual = memberRepository.findByEmailAndPassword(email, password);

        //then
        assertThat(actual).isPresent();
    }

    @ParameterizedTest
    @CsvSource(value = {"test@email.com,12345", "testtest@email.com,1234", "testtest@email.com,12345"})
    @DisplayName("맞지 않는 email 또는 password를 기반으로 멤버 데이터를 조회하면 empty 이다")
    void findByNotExistedEmailOrPassword(String email, String password) {
        //when
        Optional<Member> actual = memberRepository.findByEmailAndPassword(email, password);

        //then
        assertThat(actual).isEmpty();
    }

    @ParameterizedTest
    @CsvSource(value = {"member@email.com,true", "testtest@email.com,false"})
    @DisplayName("email 을 기반으로 멤버 데이터가 존재하는지 확인한다")
    void existsByEmail(String email, boolean expected) {
        //when
        boolean actual = memberRepository.existsByEmail(email);

        //then
        assertThat(actual).isEqualTo(expected);
    }
}
