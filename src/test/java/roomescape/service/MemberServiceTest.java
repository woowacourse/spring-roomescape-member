package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.member.MemberResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MemberServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password) VALUES ('user1', 'user1@wooteco.com', 'user1')");
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password) VALUES ('user2', 'user2@wooteco.com', 'user2')");
    }

    @Test
    @DisplayName("모든 회원을 조회한다.")
    void findAll() {
        List<MemberResponse> members = memberService.findAll();

        assertThat(members).hasSize(2);
    }
}
