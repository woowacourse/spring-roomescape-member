package roomescape.business.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import roomescape.business.Member;
import roomescape.exception.MemberException;
import roomescape.infrastructure.JwtProvider;
import roomescape.persistence.MemberRepository;
import roomescape.presentation.dto.request.LoginRequestDto;

@Named
public class AuthenticationService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Inject
    public AuthenticationService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public String login(LoginRequestDto loginRequestDto) {
        Member member = memberRepository.findByEmail(loginRequestDto.email())
                .orElseThrow(() -> new MemberException("존재하지 않는 사용자입니다."));

        if (!member.checkValidPassword(loginRequestDto.password())) {
            throw new MemberException("비밀번호가 일치하지 않습니다.");
        }

        return jwtProvider.createToken(member.getId().toString());
    }

    public Member findMemberByToken(String token) {
        if (!jwtProvider.validateToken(token)) {
            throw new MemberException("유효하지 않은 토큰입니다.");
        }
        Long payload = Long.valueOf(jwtProvider.getPayload(token));
        return memberRepository.findById(payload)
                .orElseThrow(() -> new MemberException("존재하지 않는 사용자입니다."));
    }
}
