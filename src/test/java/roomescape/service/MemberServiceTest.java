package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.FakeMemberDaoImpl;
import roomescape.domain.member.dao.MemberDao;
import roomescape.domain.member.dto.request.MemberRequestDto;
import roomescape.domain.member.model.Member;
import roomescape.domain.member.service.MemberService;
import roomescape.global.exception.DuplicateException;
import roomescape.global.exception.NotFoundException;

public class MemberServiceTest {

    private MemberService memberService;
    private MemberDao memberDao;

    @BeforeEach
    void init() {
        memberDao = new FakeMemberDaoImpl();
        memberService = new MemberService(memberDao);
    }

    @Test
    @DisplayName("save 과정에서 이미 회원이 존재한다면, 예외가 발생해야 한다.")
    void already_exist_member_then_throw_exception() {
        MemberRequestDto memberRequestDto = new MemberRequestDto(
            "젠슨",
            "email.com",
            "aaa"
        );
        memberService.saveMember(memberRequestDto);
        assertThatThrownBy(() -> memberService.saveMember(memberRequestDto))
            .isInstanceOf(DuplicateException.class);
    }

    @Test
    @DisplayName("memberId를 통해 유저를 가져올 수 있어야 한다.")
    void get_member_from_member_id() {
        Member member = Member.createUser("젠슨", "aaa@email.com", "bbb");
        long savedId = memberDao.save(member);
        member.setId(savedId);
        Member findMember = memberService.getMemberFrom(savedId);
        assertThat(findMember.getEmail()).isEqualTo("aaa@email.com");
    }

    @Test
    @DisplayName("memberId를 통해 유저를 가져올 때, memberId가 존재하지 않는다면 예외가 발생해야 한다.")
    void invalid_member_id_then_throw_exception() {
        assertThatThrownBy(() -> memberService.getMemberFrom(99999999L))
            .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("이메일과 패스워드를 통해 유저를 가져올 수 있어야 한다.")
    void get_member_of_email_and_password() {
        String email = "aaa@email.com";
        String password = "bbb";
        Member member = Member.createUser("젠슨", email, password);
        long savedId = memberDao.save(member);
        member.setId(savedId);
        Member findMember = memberService.getMemberOf(email, password);
        assertThat(findMember.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("이메일과 패스워드를 통해 유저를 가져올 때, 해당하는 이메일과 패스워드가 없다면 예외가 발생해야 한다.")
    void invalid_email_and_password_then_throw_exception() {
        assertThatThrownBy(() -> memberService.getMemberOf("email123@email.com", "aabbcc"))
            .isInstanceOf(NotFoundException.class);
    }
}

