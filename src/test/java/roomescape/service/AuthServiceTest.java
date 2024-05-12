package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dao.MemberDAO;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.dto.TokenResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthServiceTest {

    @Autowired
    AuthService authService;
    @Autowired
    MemberDAO memberDAO;

    @Test
    @DisplayName("발급된 토큰을 통해 멤버를 찾을 수 있다.")
    void findMemberByToken() {
        memberDAO.insert(new Member("뽀로로", "email@email.com", "1234"));
        final TokenResponse tokenResponse = authService.createToken(new LoginRequest("email@email.com", "1234"));

        final Member member = authService.findMemberByToken(tokenResponse.getAccessToken());

        assertThat(member.getName()).isEqualTo("뽀로로");
    }
}
