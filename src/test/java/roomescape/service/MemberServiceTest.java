package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Member;
import roomescape.dto.MemberRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("회원가입을 할 수 있다.")
    void join() {
        final Member joinedMember = memberService.join(new MemberRequest("뽀로로", "1234@email.com", "1234"));

        assertThat(joinedMember).isNotNull();
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입을 하려 하면 예외가 발생한다.")
    void invalidJoin() {
        memberService.join(new MemberRequest("1234@email.com", "1234", "뽀로로"));

        assertThatThrownBy(() -> memberService.join(new MemberRequest("1234@email.com", "aaaa", "제이")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 이메일입니다.");
    }
}
