package roomescape.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.persistence.MemberRepository;
import roomescape.service.request.MemberLoginRequest;
import roomescape.service.response.MemberResponse;
import roomescape.service.response.Token;
import roomescape.utils.JwtTokenProvider;
import roomescape.web.exception.AuthenticationException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Token login(MemberLoginRequest request) {
        Member findMember = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new AuthenticationException("올바르지 않은 회원 정보입니다."));
        return new Token(jwtTokenProvider.createToken(findMember));
    }

    public MemberResponse getMemberInfo(Cookie[] cookies) {
        validateCookie(cookies);
        String token = getToken(cookies);
        String tokenSubject = jwtTokenProvider.getTokenSubject(token);
        Long id = Long.parseLong(tokenSubject);

        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new AuthenticationException("올바르지 않은 회원 정보입니다."));

        return new MemberResponse(findMember.getNameValue());
    }

    private void validateCookie(Cookie[] cookies) {
        if (cookies == null) {
            throw new AuthenticationException("로그인된 회원 정보가 없습니다.");
        }
    }

    private String getToken(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        return "";
    }
}
