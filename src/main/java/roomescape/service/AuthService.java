package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.common.exception.AuthException.LoginAuthException;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.service.param.LoginParam;
import roomescape.service.result.LoginResult;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public AuthService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public LoginResult login(LoginParam loginParam) {
        Member member = memberRepository.findByEmail(loginParam.email())
                .orElseThrow(() -> new LoginAuthException(loginParam.email() + "에 해당하는 멤버가 존재하지 않습니다."));
        if (member.isNotPassword(loginParam.password())) {
            throw new LoginAuthException(loginParam.email() + " 사용자의 비밀번호가 같지 않습니다.");
        }
        return new LoginResult(jwtProvider.issue(member.getId()));
    }
}
