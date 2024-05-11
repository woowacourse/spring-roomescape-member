package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.dto.MemberModel;
import roomescape.dto.request.MemberCreateRequest;
import roomescape.dto.request.MemberFindRequest;
import roomescape.dto.response.MemberResponse;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {
    @Mock
    MemberDao memberDao;

    @InjectMocks
    MemberService memberService;

    private final Member member = new Member(1L, "켬미", "aaa@naver.com");

    @DisplayName("이메일와 비밀번호를 통해 멤버를 읽을 수 있다.")
    @Test
    void readMember_byEmailAndPassword() {
        when(memberDao.readMemberByEmailAndPassword(any(String.class), any(String.class)))
                .thenReturn(Optional.of(member));

        MemberFindRequest request = new MemberFindRequest("aaa@naver.com", "1111");
        MemberModel actual = memberService.readMember(request);
        assertThat(actual).isEqualTo(MemberModel.from(member));
    }

    @DisplayName("해당 id를 통해 멤버를 읽을 수 있다.")
    @Test
    void readMember_byId() {
        when(memberDao.readMemberById(any(Long.class)))
                .thenReturn(Optional.of(member));

        MemberResponse actual = memberService.readMember(1L);
        assertThat(actual).isEqualTo(new MemberResponse(member.name()));
    }

    @DisplayName("멤버를 추가할 수 있다.")
    @Test
    void createMember() {
        when(memberDao.createMember(any(Member.class), any(String.class)))
                .thenReturn(member);

        MemberCreateRequest request = new MemberCreateRequest("켬미", "aaa@naver.com", "1111");
        assertThatCode(() -> memberService.createMember(request))
                .doesNotThrowAnyException();
    }
}
