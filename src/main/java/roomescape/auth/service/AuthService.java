package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.exception.InvalidPasswordException;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequest;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public String createToken(MemberRequest memberRequest) {
        Member member = memberRepository.readByEmail(memberRequest.email());

        validatePassword(member.getPassword(), memberRequest.password());

        return jwtTokenProvider.createToken(member);
    }

    public Member readByToken(String token) {
        String email = jwtTokenProvider.getEmail(token);

        return memberRepository.readByEmail(email);
    }

    private void validatePassword(String actualPassword, String expectedPassword) {
        if (!actualPassword.equals(expectedPassword)) {
            throw new InvalidPasswordException("패스워드가 올바르지 않습니다.");
        }
    }
}
