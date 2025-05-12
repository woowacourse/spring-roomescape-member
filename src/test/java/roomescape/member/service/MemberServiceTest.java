package roomescape.member.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.exception.ConflictException;
import roomescape.exception.ExceptionCause;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberSignupRequest;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    JwtUtil jwtUtil;

    @Mock
    MemberDao memberDao;

    @InjectMocks
    MemberService memberService;

    @DisplayName("회원가입시, 동일한 이메일이 존재하면 예외가 발생한다.")
    @Test
    void signup() {

        // given
        String email = "test@test.com";
        when(memberDao.findByEmail(email)).thenReturn(Optional.of(mock(Member.class)));

        // when
        assertThatThrownBy(() -> memberService.signup(new MemberSignupRequest(email, "1234qwer!", "test")))
                .isInstanceOf(ConflictException.class)
                .hasMessage(ExceptionCause.MEMBER_EXIST.getMessage());
    }
}