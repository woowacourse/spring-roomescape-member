package roomescape.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.LoginSession;
import roomescape.domain.Member;
import roomescape.dto.LoginInfo;
import roomescape.dto.request.AuthRequest;
import roomescape.error.AuthenticationException;
import roomescape.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final LoginSession loginSession;

    public void login(final AuthRequest request, final HttpSession session) {
        final Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 이메일입니다."));

        if (!member.getPassword().equals(request.password())) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        final LoginInfo loginInfo = new LoginInfo(member.getId(), member.getName());
        loginSession.setLoginInfo(session, loginInfo);
    }

    public void logout(final HttpSession session) {
        loginSession.clear(session);
    }
}
