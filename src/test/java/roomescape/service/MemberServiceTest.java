package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.exception.ClientErrorExceptionWithData;

@Sql("/member-test-data.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void 존재하지_않는_id로_조회할_경우_예외_발생() {
        //given, when, then
        String sql = "SELECT COUNT(*) FROM member";
        int count = jdbcTemplate.queryForObject(sql, Integer.class);
        Long notExistId = (long) (count + 1);

        //when, then
        assertThatThrownBy(() -> memberService.getMemberById(notExistId))
                .isInstanceOf(ClientErrorExceptionWithData.class);
    }

    @Test
    void 존재하지_않는_email로_조회할_경우_예외_발생() {
        //given, when, then
        assertThatThrownBy(() -> memberService.getMemberByEmail("notExistEmail"))
                .isInstanceOf(ClientErrorExceptionWithData.class);
    }
}
