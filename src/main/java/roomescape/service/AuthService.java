package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.entity.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.dto.LoginInfo;
import roomescape.dto.request.AuthRequest;
import roomescape.error.AuthenticationException;
import roomescape.infra.SessionLoginRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final SessionLoginRepository sessionLoginRepository;

    public void login(final AuthRequest request) {
        final Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 이메일입니다."));
        member.validatePassword(request.password());

        final LoginInfo loginInfo = new LoginInfo(member.getId(), member.getName(), member.getRole());
        sessionLoginRepository.setLoginInfo(loginInfo);
    }

    public void logout() {
        sessionLoginRepository.clear();
    }
}
