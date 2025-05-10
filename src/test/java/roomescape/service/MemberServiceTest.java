package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.meber.MemberDao;
import roomescape.domain.Member;
import roomescape.support.auth.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberDao memberDao;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private MemberService memberService;

    @Test
    void findMemberByTokenTest() {

        // given
        final Member member = new Member("체체", "cheche903@naver.com", "password1234");
        final String token = "token";
        when(jwtTokenProvider.getPayload(token)).thenReturn("cheche903@naver.com");
        when(memberDao.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        // when
        assertThat(memberService.findByToken(token).name()).isEqualTo("체체");
    }
}
