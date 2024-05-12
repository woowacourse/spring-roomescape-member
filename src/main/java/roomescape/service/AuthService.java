package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.auth.LoginRequest;
import roomescape.global.util.TokenManager;
import roomescape.repository.MemberRepository;

import java.util.Map;

@Service
public class AuthService {

    private final MemberRepository memberRepository;

    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String login(LoginRequest loginRequest) {
        // TODO: 암호화
        Member member = memberRepository.findByEmailAndPassword(loginRequest.email(), loginRequest.password());

        String subject = member.getId().toString();
        Map<String, String> claims = Map.of(
                "name", member.getName()
        );

        return TokenManager.generateToken(subject, claims);
    }
}
