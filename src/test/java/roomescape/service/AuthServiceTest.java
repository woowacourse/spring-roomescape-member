package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.member.Member;
import roomescape.dto.member.MemberLoginRequest;

class AuthServiceTest extends BaseServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) VALUES ('user1', 'user1@wooteco.com', 'user1', 'USER')");
    }

    @Test
    @DisplayName("토큰으로 사용자를 찾는다.")
    void findMemberByToken() {
        String token = authService.createToken(MemberLoginRequest.of("user1", "user1@wooteco.com"));
        Member result = authService.findMemberByToken(token);

        assertThat(result.getName().getValue()).isEqualTo("user1");
    }
}
