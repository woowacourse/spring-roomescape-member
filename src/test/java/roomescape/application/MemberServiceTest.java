package roomescape.application;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.BaseTest;
import roomescape.domain.Member;
import roomescape.fixture.MemberDbFixture;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberServiceTest extends BaseTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberDbFixture memberDbFixture;

    @Test
    void 이메일로_회원을_조회한다() {
        Member member = memberDbFixture.듀이();
        Member findMember = memberService.findMemberByEmail(member.getEmail());

        assertThat(member).isEqualTo(findMember);
    }

    @Test
    void 존재하지_않는_이메일로_조회하면_예외가_발생한다() {
        String notExistEmail = "email@email.com";
        assertThatThrownBy(() -> memberService.findMemberByEmail(notExistEmail))
                .isInstanceOf(NoSuchElementException.class);
    }
}
