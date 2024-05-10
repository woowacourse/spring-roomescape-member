package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.domain.Member;
import roomescape.persistence.MemberRepository;
import roomescape.service.request.LoginMember;
import roomescape.service.request.MemberLoginRequest;
import roomescape.service.response.MemberResponse;
import roomescape.service.response.Token;
import roomescape.web.exception.AuthenticationException;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Token login(MemberLoginRequest request) {
        Member findMember = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new AuthenticationException("올바르지 않은 회원 정보입니다."));
        return new Token(jwtTokenProvider.createToken(findMember));
    }

    public LoginMember getLoginMember(String tokenValue) {
        String tokenSubject = jwtTokenProvider.getTokenSubject(tokenValue);
        Long id = Long.parseLong(tokenSubject);

        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException("올바르지 않은 회원 정보입니다."));

        return new LoginMember(findMember.getId());
    }

    public MemberResponse getLoginMemberInfo(LoginMember loginMember) {
        Member findMember = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new AuthenticationException("올바르지 않은 회원 정보입니다."));
        return new MemberResponse(findMember.getId(), findMember.getNameValue());
    }
}
