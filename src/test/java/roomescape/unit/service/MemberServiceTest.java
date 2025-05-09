package roomescape.unit.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static roomescape.common.Constant.FIXED_CLOCK;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberEncodedPassword;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;
import roomescape.domain.theme.LastWeekRange;
import roomescape.domain.theme.Theme;
import roomescape.domain.theme.ThemeDescription;
import roomescape.domain.theme.ThemeName;
import roomescape.domain.theme.ThemeThumbnail;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;
import roomescape.service.MemberService;
import roomescape.service.ThemeService;
import roomescape.service.request.CreateThemeRequest;
import roomescape.service.response.MemberResponse;
import roomescape.service.response.ThemeResponse;

public class MemberServiceTest {

    private MemberRepository memberRepository = mock(MemberRepository.class);
    private MemberService memberService = new MemberService(memberRepository);

    @Test
    void 모든_멤버를_조회한다() {
        // given
        List<Member> members = List.of(
                new Member(
                        1L,
                        new MemberName("짭한스1"),
                        new MemberEmail("leehyeonsu4848@gmail.com"),
                        new MemberEncodedPassword("gdgd"),
                        MemberRole.MEMBER
                ),
                new Member(
                        2L,
                        new MemberName("짭한스2"),
                        new MemberEmail("leehyeonsu488@gmail.com"),
                        new MemberEncodedPassword("gdgdsad"),
                        MemberRole.MEMBER
                )
        );
        when(memberRepository.findAll()).thenReturn(members);

        // when
        List<MemberResponse> allMembers = memberService.findAllMembers();

        // then
        assertThat(allMembers).containsExactly(
                new MemberResponse(members.get(0).getId(), members.get(0).getName().name()),
                new MemberResponse(members.get(1).getId(), members.get(1).getName().name())
        );
    }
}
