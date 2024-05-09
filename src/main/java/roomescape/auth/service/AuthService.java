package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.exception.InvalidPasswordException;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public Member readByToken(String token) {
        String email = jwtTokenProvider.getPayload(token);

        return memberRepository.read(email);
    }

    public String createToken(TokenRequest tokenRequest) {
        Member member = memberRepository.read(tokenRequest.email());

        validatePassword(member.getPassword(), tokenRequest.password());

        return jwtTokenProvider.createToken(member.getEmail());
    }

    public void validatePassword(String actualPassword, String expectedPassword) {
        if (!actualPassword.equals(expectedPassword)) {
            throw new InvalidPasswordException("패스워드가 올바르지 않습니다.");
        }
    }
}
