package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.member.MemberCreateRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.service.exception.InvalidRequestException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class MemberServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MemberService memberService;

    @Nested
    @DisplayName("회원 조회")
    class FindMember {

        @Test
        @DisplayName("모든 회원을 조회한다.")
        void findAll() {
            jdbcTemplate.update(
                    "INSERT INTO member (name, email, password, role) VALUES ('user1', 'user1@wooteco.com', 'user1', 'USER')");
            jdbcTemplate.update(
                    "INSERT INTO member (name, email, password, role) VALUES ('user2', 'user2@wooteco.com', 'user2', 'USER')");

            List<MemberResponse> members = memberService.findAll();

            assertThat(members).hasSize(2);
        }
    }

    @Nested
    @DisplayName("회원 생성")
    class CreateMember {

        @Test
        @DisplayName("회원을 생성한다")
        void add() {
            MemberCreateRequest request = MemberCreateRequest.from("newUser@wooteco.com", "newUser", "newUser");

            MemberResponse member = memberService.add(request);

            assertThat(member.name()).isEqualTo("newUser");
        }

        @Test
        @DisplayName("이미 존재하는 이메일로 회원 생성을 시도하면 예외가 발생한다.")
        void addByDuplicateEmail() {
            jdbcTemplate.update(
                    "INSERT INTO member (name, email, password, role) VALUES ('user1', 'user1@wooteco.com', 'user1', 'USER')");
            MemberCreateRequest request = MemberCreateRequest.from("user1@wooteco.com", "newUser", "newUser");

            assertThatThrownBy(() -> memberService.add(request))
                    .isInstanceOf(InvalidRequestException.class);
        }
    }
}
