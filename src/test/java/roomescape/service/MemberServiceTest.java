package roomescape.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Member;
import roomescape.dto.MemberDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberServiceTest {

    @Autowired
    private final MemberService memberService;

    @Autowired
    public MemberServiceTest(MemberService memberService) {
        this.memberService = memberService;
    }

    @Test
    void registerTest() {
        Member member = memberService.register(new MemberDto("name", "email@email.com", "password"));

        assertThat(member).isNotNull();
    }

    @Test
    void loginTest() {
        String id = "email@email.com";
        String password = "password";
        memberService.register(new MemberDto("name", id, password));

        Member member = memberService.login(id, password);

        assertThat(member.isEmailMatches(id)).isTrue();
        assertThat(member.isPasswordMatches(password)).isTrue();
    }
}
