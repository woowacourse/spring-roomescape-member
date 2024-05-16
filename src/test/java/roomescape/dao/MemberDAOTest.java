package roomescape.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberDAOTest {

    @Autowired
    MemberDAO memberDAO;

    @Test
    @DisplayName("멤버를 추가한다.")
    void insert() {
        final Member member = new Member("뽀로로", "1234@email.com", "1234");

        final Member savedMember = memberDAO.insert(member);

        assertThat(savedMember.getName()).isEqualTo("뽀로로");
    }

    @ParameterizedTest
    @CsvSource(value = {"1234@email.com, 1234, true", "nothing@email.com, nothing, false"})
    @DisplayName("이메일과 비밀번호가 일치하는 회원을 찾을 수 있다.")
    void existMember(String email, String password, boolean expected) {
        final Member member = new Member("뽀로로", "1234@email.com", "1234");
        memberDAO.insert(member);

        final Boolean actual = memberDAO.existMember(email, password);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("이메일을 통해 멤버를 조회한다.")
    void findByEmail() {
        final Member member = new Member("뽀로로", "1234@email.com", "1234");
        memberDAO.insert(member);

        final Member findMember = memberDAO.findByEmail("1234@email.com");

        assertThat(findMember.getName()).isEqualTo("뽀로로");
    }

    @Test
    @DisplayName("아이디를 통해 멤버를 조회한다.")
    void findById() {
        final Member member = new Member("뽀로로", "1234@email.com", "1234");
        final Member savedMember = memberDAO.insert(member);

        final Member findMember = memberDAO.findById(savedMember.getId());

        assertThat(findMember.getName()).isEqualTo("뽀로로");
    }

    @Test
    @DisplayName("전체 멤버를 조회한다.")
    void findAll() {
        final Member member1 = new Member("뽀로로1", "1234@email.com", "1234");
        final Member member2 = new Member("뽀로로2", "12345@email.com", "12345");
        memberDAO.insert(member1);
        memberDAO.insert(member2);

        final List<Member> members = memberDAO.findAll();

        assertThat(members).hasSize(2);
    }
}
