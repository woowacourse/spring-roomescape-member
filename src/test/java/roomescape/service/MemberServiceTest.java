package roomescape.service;

import org.junit.jupiter.api.Test;
import roomescape.domain.MemberRole;
import roomescape.exception.NotFoundMemberException;
import roomescape.fake.FakeMemberRepository;
import roomescape.persistence.query.CreateMemberQuery;
import roomescape.service.param.LoginMemberParam;
import roomescape.service.param.RegisterMemberParam;
import roomescape.service.result.MemberResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberServiceTest {

    private final FakeMemberRepository memberRepository = new FakeMemberRepository();
    private final MemberService memberService = new MemberService(memberRepository);

    @Test
    void 존재하지_않는_email과_password로_로그인_시_예외() {
        //given
        LoginMemberParam loginMemberParam = new LoginMemberParam("email2", "password2");

        //when & then
        assertThatThrownBy(() -> memberService.login(loginMemberParam))
                .isInstanceOf(NotFoundMemberException.class)
                .hasMessageContaining(loginMemberParam.email() + " " + loginMemberParam.password() + "에 해당하는 유저가 없습니다.");
    }

    @Test
    void email과_password로_로그인_가능() {
        //given
        memberRepository.create(new CreateMemberQuery("name1", MemberRole.USER, "email1", "password1"));
        LoginMemberParam loginMemberParam = new LoginMemberParam("email1", "password1");

        //when & then
        assertThat(memberService.login(loginMemberParam)).isEqualTo(new MemberResult(1L, "name1", MemberRole.USER, "email1"));
    }

    @Test
    void 멤버를_생성할_수_있다() {
        assertThat(memberService.create(new RegisterMemberParam("name1", "email1", "password1")))
                .isEqualTo(new MemberResult(1L, "name1", MemberRole.USER, "email1"));
    }

    @Test
    void id를_통해_멤버를_찾을_수_있다() {
        //given
        memberRepository.create(new CreateMemberQuery("name1", MemberRole.USER, "email1", "password1"));

        //when & then
        assertThat(memberService.findById(1L)).isEqualTo(new MemberResult(1L, "name1", MemberRole.USER, "email1"));
    }

    @Test
    void id를_통해_멤버를_찾으려_할_때_해당하는_id의_멤버가_존재하지_않으면_예외() {
        assertThatThrownBy(() -> memberService.findById(1L))
                .isInstanceOf(NotFoundMemberException.class)
                .hasMessage(1L + "에 해당하는 유저가 없습니다.");
    }
}