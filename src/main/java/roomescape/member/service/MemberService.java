package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.exception.InvalidPasswordException;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequest;
import roomescape.member.infrastructure.JwtTokenProvider;
import roomescape.member.repository.MemberRepository;

import java.util.List;

@Service
public class MemberService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public MemberService(JwtTokenProvider jwtTokenProvider, MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public Member readByToken(String token) {
        String email = jwtTokenProvider.getPayload(token);

        return memberRepository.read(email);
    }

    public String createToken(MemberRequest memberRequest) {
        Member member = memberRepository.read(memberRequest.email());

        validatePassword(member.getPassword(), memberRequest.password());

        return jwtTokenProvider.createToken(member.getEmail());
    }

    public List<Member> readAll() {
        return memberRepository.readAll();
    }

    public void validatePassword(String actualPassword, String expectedPassword) {
        if (!actualPassword.equals(expectedPassword)) {
            throw new InvalidPasswordException("패스워드가 올바르지 않습니다.");
        }
    }
}
