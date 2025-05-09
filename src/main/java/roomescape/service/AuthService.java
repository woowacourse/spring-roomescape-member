package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.controller.member.dto.LoginCheckResponse;
import roomescape.controller.member.dto.MemberLoginRequest;
import roomescape.exception.AuthorizationException;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.model.Member;
import roomescape.repository.MemberRepository;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public String login(final MemberLoginRequest request) {
        final Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new AuthorizationException("해당 이메일에 대한 회원 정보가 존재하지 않습니다. 이메일: " + request.email()));
        if (!member.password().equals(request.password())) {
            throw new AuthorizationException("비밀번호가 틀립니다.");
        }
        return jwtTokenProvider.createToken(member);
    }

    public LoginCheckResponse checkLogin(final String token) {
        if (token.isEmpty() || !jwtTokenProvider.validateToken(token)) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
        final String email = jwtTokenProvider.getPayload(token);
        final Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthorizationException("해당 이메일에 대한 회원 정보가 존재하지 않습니다. 이메일: " + email));
        return new LoginCheckResponse(member.name());
    }
}
