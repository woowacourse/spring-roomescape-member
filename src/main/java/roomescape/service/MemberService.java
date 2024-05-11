package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.JwtTokenProvider;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.repository.JdbcMemberRepository;

@Service
public class MemberService {

    private final JdbcMemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(JdbcMemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // TODO: 예외처리 통일하기
    public Member getMemberByLogin(LoginRequest request) {
        Optional<Member> member = memberRepository.findByEmailAndPassword(request.email(), request.password());
        if (member.isEmpty()) {
            throw new AuthorizationException("등록되지 않은 사용자입니다.");
        }

        return member.get();
    }

    public Member getMemberByToken(String token) {
        long id = jwtTokenProvider.getIdFromToken(token);
        Member member = memberRepository.findById(id);

        return member;
    }
}
