package roomescape.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.auth.service.dto.LoginRequest;
import roomescape.auth.service.dto.LoginResponse;

@Service
public class AuthService {
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    @Autowired
    public AuthService(TokenProvider tokenProvider, MemberRepository memberRepository) {
        this.tokenProvider = tokenProvider;
        this.memberRepository = memberRepository;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.getByEmail(loginRequest.email());
        String token = tokenProvider.create(member);
        return new LoginResponse(token);
    }
}
