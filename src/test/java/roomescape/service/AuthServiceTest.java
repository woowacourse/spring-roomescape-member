package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.Member;
import roomescape.dto.member.UserLoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password) VALUES ('user1', 'user1@wooteco.com', 'user1')");
    }

    @Test
    @DisplayName("이메일과 비밀번호로 사용자를 찾는다.")
    void findMemberByEmailAndPassword() {
        Member result = authService.findMember(UserLoginRequest.of("user1", "user1@wooteco.com"));

        assertThat(result.getName()).isEqualTo("user1");
    }

    @Test
    @DisplayName("아이디로 사용자를 찾는다.")
    void findMemberById() {
        Member result = authService.findMember(1L);

        assertThat(result.getName()).isEqualTo("user1");
    }

    @Test
    @DisplayName("토큰으로 사용자를 찾는다.")
    void findMemberByToken() {
        String token = authService.createToken(UserLoginRequest.of("user1", "user1@wooteco.com"));
        Member result = authService.findMemberByToken(token);

        assertThat(result.getName()).isEqualTo("user1");
    }
}
