package roomescape.service.auth;

import org.springframework.stereotype.Service;
import roomescape.exception.AuthenticationException;
import roomescape.repository.MemberRepository;
import roomescape.service.dto.request.LoginRequest;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public String login(LoginRequest request) {
        boolean isUser = memberRepository.existsByEmailAndPassword(request.email(), request.password());
        if(isUser) {
            return tokenProvider.generateAccessToken(request);
        }
        throw new AuthenticationException("잘못된 로그인 정보입니다.");
    }
}
