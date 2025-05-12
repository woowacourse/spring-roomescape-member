package roomescape.auth.service;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginMember;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenWithCookieResponse;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.global.security.jwt.JwtTokenProvider;
import roomescape.member.entity.Member;
import roomescape.member.repository.MemberRepository;

import java.util.NoSuchElementException;

@Service
public class AuthService {

    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_MAX_AGE = 600;

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenWithCookieResponse issueToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.email())
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다 " + tokenRequest.email()));

        if (!member.matchesPassword(tokenRequest.password())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(member);
        ResponseCookie responseCookie = createJwtCookie(accessToken);
        return new TokenWithCookieResponse(accessToken, responseCookie);
    }

    public LoginCheckResponse checkLogin(LoginMember loginMember) {
        Member member = memberRepository.findById(loginMember.id())
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다"));
        return LoginCheckResponse.from(member);
    }

    private ResponseCookie createJwtCookie(String token) {
        return ResponseCookie.from("jwt_token", token)
                .httpOnly(true)
                .secure(false)
                .path(COOKIE_PATH)
                .maxAge(COOKIE_MAX_AGE)
                .build();
    }
}
