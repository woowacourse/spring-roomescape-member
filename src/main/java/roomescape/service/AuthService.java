package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.token.JwtProvider;

@Service
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    public AuthService(JwtProvider jwtProvider, MemberService memberService) {
        this.jwtProvider = jwtProvider;
        this.memberService = memberService;
    }
}
