package roomescape.service;

import jakarta.servlet.http.Cookie;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.LoginMember;
import roomescape.dto.member.LoginMemberResponse;
import roomescape.dto.member.LoginRequest;
import roomescape.exception.InvalidCredentialsException;
import roomescape.repository.MemberRepository;
import roomescape.util.TokenProvider;

@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final TokenProvider jwtTokenProvider;

    public LoginService(MemberRepository memberRepository, MemberService memberService, TokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Cookie createLoginCookie(LoginRequest loginRequest) {
        if (isInvalidLogin(loginRequest.email(), loginRequest.password())) {
            throw new InvalidCredentialsException("[ERROR] 로그인 정보를 다시 확인해 주세요.");
        }

        String token = jwtTokenProvider.createToken(loginRequest.email());
        return createCookie(token);
    }

    private Cookie createCookie(String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }

    private boolean isInvalidLogin(String email, String password) {
        Optional<LoginMember> member = memberRepository.findByEmailAndPassword(email, password);
        return member.isEmpty();
    }

    public LoginMemberResponse findMemberByToken(String token) {
        LoginMember loginMember = memberService.findMemberByToken(token);
        return new LoginMemberResponse(loginMember.getId(), loginMember.getName());
    }

    public Cookie setLogoutCookie() {
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setAttribute("SameSite", "Strict");
        return cookie;
    }
}
