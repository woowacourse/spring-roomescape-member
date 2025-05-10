package roomescape.auth.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenWithCookieResponse;
import roomescape.auth.exception.UnauthorizedException;
import roomescape.common.security.jwt.JwtTokenProvider;
import roomescape.common.util.CookieUtil;
import roomescape.member.entity.Member;
import roomescape.member.repository.MemberRepository;

import java.util.Arrays;
import java.util.NoSuchElementException;

@Service
public class AuthService {

    private static final String JWT_COOKIE_NAME = "jwt_token";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public TokenWithCookieResponse issueToken(TokenRequest tokenRequest) {
        Member member = memberRepository.findByEmail(tokenRequest.getEmail())
                .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다 " + tokenRequest.getEmail()));

        if (!member.matchesPassword(tokenRequest.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(member);
        ResponseCookie responseCookie = CookieUtil.createJwtCookie(accessToken);
        return new TokenWithCookieResponse(accessToken, responseCookie);
    }

    public LoginCheckResponse checkLogin(HttpServletRequest request) {
        String token = extractTokenFromCookies(request.getCookies());

        try {
            Long memberId = jwtTokenProvider.getMemberId(token);
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new NoSuchElementException("해당 사용자를 찾을 수 없습니다"));
            return LoginCheckResponse.from(member);
        } catch (ExpiredJwtException e) {
            throw new UnauthorizedException("토큰이 만료되었습니다.");
        } catch (JwtException e) {
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }

    private String extractTokenFromCookies(Cookie[] cookies) {
        if (cookies == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(JWT_COOKIE_NAME))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new UnauthorizedException("유효한 토큰이 존재하지 않습니다."));
    }
}
