package roomescape.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.LoginInfo;
import roomescape.dto.request.AuthRequest;
import roomescape.error.AccessDeniedException;
import roomescape.error.AuthenticationException;
import roomescape.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String LOGIN_INFO_KEY = "loginInfo";

    private final MemberRepository memberRepository;

    public void login(final AuthRequest request, final HttpSession session) {
        final Member member = memberRepository.findByEmail(request.email()).orElseThrow(
                () -> new AuthenticationException("존재하지 않는 이메일입니다."));
        if (!member.getPassword().equals(request.password())) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }

        final LoginInfo loginInfo = new LoginInfo(member.getId(), member.getName());
        session.setAttribute(LOGIN_INFO_KEY, loginInfo);
    }

    public void logout(final HttpSession session) {
        if (session.getAttribute(LOGIN_INFO_KEY) == null) {
            throw new AccessDeniedException("로그인 정보가 없습니다.");
        }
        session.removeAttribute(LOGIN_INFO_KEY);
    }

    public LoginInfo checkLogin(final HttpSession session) {
        final LoginInfo loginInfo = (LoginInfo) session.getAttribute(LOGIN_INFO_KEY);
        if (loginInfo == null) {
            throw new AccessDeniedException("로그인 정보가 없습니다.");
        }
        return loginInfo;
    }
}
