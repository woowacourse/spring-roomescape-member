package roomescape.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import roomescape.common.exception.AuthenticationException;
import roomescape.member.controller.dto.LoginCheckResponse;
import roomescape.member.controller.dto.LoginRequest;
import roomescape.member.auth.dto.MemberInfo;
import roomescape.member.controller.dto.SignupRequest;
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
    private final PasswordEncoder passwordEncoder;

    public MemberInfo signup(SignupRequest signupRequest) {
        return memberService.create(new SignupRequest(
                signupRequest.email(),
                passwordEncoder.encode(signupRequest.password()),
                signupRequest.name()
        ));
    }

    public String login(LoginRequest loginRequest) {
        final Account account = memberService.findAccount(loginRequest);
        if (!account.isSamePassword(passwordEncoder, loginRequest.password())) {
            throw new AuthenticationException("비밀번호가 일치하지 않습니다.");
        }
        return jwtTokenProvider.generateToken(account);
    }

    public LoginCheckResponse checkLogin(final String token) {
        final MemberName name = jwtTokenExtractor.extractMemberNameFromToken(token);
        return new LoginCheckResponse(name.getValue());
    }

    public MemberInfo getMemberInfo(final String token) {
        return MemberConverter.toDto(memberService.get(jwtTokenExtractor.extractMemberIdFromToken(token)));
    }

    public Member get(final String token) {
        return memberService.get(jwtTokenExtractor.extractMemberIdFromToken(token));
    }
}
