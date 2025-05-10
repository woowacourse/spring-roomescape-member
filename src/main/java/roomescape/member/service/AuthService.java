package roomescape.member.service;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.controller.dto.LoginCheckResponse;
import roomescape.member.controller.dto.LoginRequest;
import roomescape.member.auth.dto.MemberInfo;
import roomescape.member.domain.Account;
import roomescape.member.domain.Member;
import roomescape.member.auth.JwtTokenExtractor;
import roomescape.member.auth.JwtTokenProvider;
import roomescape.member.domain.MemberName;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenExtractor jwtTokenExtractor;

    public String login(LoginRequest loginRequest) {
        Account account = memberService.findAccount(loginRequest);
        if (!account.isSamePassword(loginRequest.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return jwtTokenProvider.generateToken(account);
    }

    public LoginCheckResponse checkLogin(Cookie[] cookies) {
        MemberName name = jwtTokenExtractor.extractMemberNameFromCookie(cookies);
        return new LoginCheckResponse(name.getValue());
    }

    public MemberInfo getMemberInfo(Cookie[] cookies) {
        Member member = memberService.get(jwtTokenExtractor.extractMemberIdFromCookie(cookies));
        return new MemberInfo(
                member.getId().getValue(),
                member.getName().getValue(),
                member.getEmail().getValue());
    }
}
