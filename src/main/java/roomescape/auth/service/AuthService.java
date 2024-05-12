package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.exception.InvalidPasswordException;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequest;
import roomescape.member.repository.MemberRepository;

import java.util.Optional;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public String createToken(MemberRequest memberRequest) {
        Optional<Member> memberOptional = memberRepository.findByEmail(memberRequest.email());

        if (memberOptional.isEmpty()) {
            throw new IllegalArgumentException("이메일 정보가 올바르지 않습니다.");
        }

        Member member = memberOptional.get();
        validatePassword(member.getPassword(), memberRequest.password());

        return jwtTokenProvider.createToken(member);
    }

    public Member findMemberByToken(String token) {
        String email = jwtTokenProvider.getEmail(token);
        Optional<Member> memberOptional = memberRepository.findByEmail(email);

        return memberOptional.orElseThrow(() -> new IllegalArgumentException("올바르지 않은 토큰입니다."));
    }

    private void validatePassword(String actualPassword, String expectedPassword) {
        if (!actualPassword.equals(expectedPassword)) {
            throw new InvalidPasswordException("패스워드가 올바르지 않습니다.");
        }
    }
}
