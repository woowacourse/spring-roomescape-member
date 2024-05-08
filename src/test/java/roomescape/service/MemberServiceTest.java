package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;
import roomescape.dto.MemberDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
public class MemberServiceTest {

    @Autowired
    private final MemberService memberService;

    @Autowired
    public MemberServiceTest(MemberService memberService) {
        this.memberService = memberService;
    }

    @Test
    void registerTest() {
        Long memberId = memberService.register(new MemberDto("name1", "email1@email.com", "password"));

        assertThat(memberId).isNotNull();
    }

    @Test
    void loginTest() {
        Member member = memberService.login("email@email.com", "password");

        assertThat(member.isEmailMatches("email@email.com")).isTrue();
        assertThat(member.isPasswordMatches("password")).isTrue();
    }
}
