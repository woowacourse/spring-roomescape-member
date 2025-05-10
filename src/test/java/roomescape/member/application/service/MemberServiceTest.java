package roomescape.member.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.application.dto.GetMemberResponse;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.infrastructure.fake.FakeMemberDao;

class MemberServiceTest {

    private final MemberService memberService;
    private final FakeMemberDao userRepository;

    MemberServiceTest() {
        this.userRepository = new FakeMemberDao();
        this.memberService = new MemberService(userRepository);
    }

    @Test
    @DisplayName("사용자 목록 조회 테스트")
    void getMembersTest() {
        // given
        userRepository.insert(new Member(0L, "name1", "email1@email.com", "password", Role.USER));
        userRepository.insert(new Member(1L, "name2", "email2@email.com", "password", Role.USER));
        userRepository.insert(new Member(2L, "name3", "email3@email.com", "password", Role.USER));

        // when
        List<GetMemberResponse> members = memberService.getMembers();

        // then
        assertThat(members).hasSize(3);
    }

}
