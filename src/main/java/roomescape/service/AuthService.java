package roomescape.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.LoginInfo;
import roomescape.domain.Member;
import roomescape.dto.request.AuthRequest;
import roomescape.error.AuthException;
import roomescape.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    public void login(final AuthRequest request, final HttpSession session) {
        final Member member = memberRepository.findByEmail(request.email()).orElseThrow(
                () -> new AuthException("존재하지 않는 이메일입니다."));
        if (!member.getPassword().equals(request.password())) {
            throw new AuthException("비밀번호가 일치하지 않습니다.");
        }

        final LoginInfo loginInfo = new LoginInfo(member.getId(), member.getName());
        session.setAttribute("loginInfo", loginInfo);
    }
}
