package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.auth.LoginRequest;
import roomescape.dto.auth.LoginResponse;
import roomescape.repository.MemberRepository;

@Service
public class AuthService {

    private final TokenManager tokenManager;
    private final MemberRepository memberRepository;

    public AuthService(TokenManager tokenManager, MemberRepository memberRepository) {
        this.tokenManager = tokenManager;
        this.memberRepository = memberRepository;
    }

    public String login(LoginRequest loginRequest) {
        // TODO: 암호화
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password());

        return tokenManager.generateToken(member.getId().toString(), "email", member.getEmail());
    }

    public LoginResponse findLoggedInMemberWithToken(String token) {
        Long memberId = Long.valueOf(tokenManager.extractSubject(token));
        Member member = memberRepository.findById(memberId);

        return LoginResponse.from(member);
    }
}
