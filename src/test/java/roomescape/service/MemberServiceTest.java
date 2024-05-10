package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.member.MemberResponse;
import roomescape.exception.ClientErrorExceptionWithLog;

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
        List<MemberResponse> allMembers = memberService.getAllMembers();
        Long notExistId = allMembers.size() + 1L;

        //when, then
        assertThatThrownBy(() -> memberService.getMemberById(notExistId))
                .isInstanceOf(ClientErrorExceptionWithLog.class);
    }

    @Test
    void 존재하지_않는_email로_조회할_경우_예외_발생() {
        //given, when, then
        assertThatThrownBy(() -> memberService.getMemberByEmail("notExistEmail"))
                .isInstanceOf(ClientErrorExceptionWithLog.class);
    }
}
