package roomescape.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import roomescape.member.Member;
import roomescape.member.dao.FakeMemberDao;
import roomescape.member.dto.response.MemberResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    private FakeMemberDao fakeMemberDao;
    private MemberService memberService;

    private final Member member1 = Member.of(1L, "포라", "sy@gmail.com", "1234", "USER");
    private final Member member2 = Member.of(2L, "라리사", "lalisa@gmail.com", "1234", "USER");
    private final Member admin = Member.of(3L, "관리자", "admin@gmail.com", "1234", "ADMIN");

    @BeforeEach
    void setUp() {
        fakeMemberDao = new FakeMemberDao(member1, member2, admin);
        memberService = new MemberService(fakeMemberDao);
    }

    @Test
    void 모든_회원을_조회할_수_있다() {
        // when
        List<MemberResponse> all = memberService.findAll();

        // then
        assertThat(all).hasSize(3);
    }
}
