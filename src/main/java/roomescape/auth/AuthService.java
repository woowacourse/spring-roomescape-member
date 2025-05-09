package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.application.MemberService;
import roomescape.application.dto.MemberDto;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.exception.AuthorizationException;
import roomescape.exception.NotFoundException;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberDto getMemberById(Long id) {
        return memberService.getMemberById(id);
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        MemberDto memberDto;
        try {
            memberDto = memberService.getMemberBy(tokenRequest.email(), tokenRequest.password());
        } catch (NotFoundException e) {
            throw new AuthorizationException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        String accessToken = jwtTokenProvider.createToken(memberDto);
        return new TokenResponse(accessToken);
    }

    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }
}
