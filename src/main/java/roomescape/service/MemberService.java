package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.auth.TokenProvider;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public String login(String email, String password) {
        Member member = memberRepository.getByEmail(email);

        if (!member.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return tokenProvider.createToken(member);
    }

    public Member findByEmail(String email) {
        return memberRepository.getByEmail(email);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
