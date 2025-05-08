package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.controller.request.LoginMemberInfo;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.exception.UnAuthorizedException;
import roomescape.service.result.LoginMemberResult;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public String createToken(LoginMemberResult loginMemberResult) {
        return jwtTokenProvider.createToken(loginMemberResult);
    }

    public Long extractSubjectFromToken(String token) {
        return jwtTokenProvider.extractIdFromToken(token);
    }

    public LoginMemberInfo findMemberByToken(final String token) {
        Long id = jwtTokenProvider.extractIdFromToken(token);
        Member member = memberRepository.findById(id).orElseThrow(UnAuthorizedException::new);

        return LoginMemberInfo.of(member.getId());
    }
}
