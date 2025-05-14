package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.exception.LoginFailException;
import roomescape.global.security.JwtProvider;
import roomescape.member.domain.Member;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public AuthService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public String login(LoginRequest request) {
        Member member = findByEmailAndPassword(request.email(), request.password());
        return jwtProvider.createToken(member);
    }

    private Member findByEmailAndPassword(String email, String password) {
        return memberRepository.findIdByEmailAndPassword(email, password)
                .orElseThrow(LoginFailException::new);
    }

    public LoginCheckResponse loginCheck(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotFoundException::new);
        return LoginCheckResponse.from(member);
    }
}
