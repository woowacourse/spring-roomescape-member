package roomescape.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Member;

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
}
