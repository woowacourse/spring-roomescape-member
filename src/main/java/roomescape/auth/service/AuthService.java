package roomescape.auth.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.Token;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.provider.model.TokenProvider;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public Token login(LoginRequest loginRequest) {
        long memberId = memberRepository.findIdByEmailAndPassword(loginRequest.email(), loginRequest.password());
        return tokenProvider.getAccessToken(memberId);
    }

    public LoginCheckResponse checkLogin(Cookie cookie) {
        long id = Long.parseLong(tokenProvider.resolveToken(cookie.getValue()));
        return new LoginCheckResponse(memberRepository.findNameById(id));
    }
}
