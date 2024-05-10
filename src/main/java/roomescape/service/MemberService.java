package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.repository.JdbcMemberRepository;

@Service
public class MemberService {

    private final JdbcMemberRepository memberRepository;

    public MemberService(JdbcMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMemberByLogin(LoginRequest request) {
        Optional<Member> member = memberRepository.findByEmailAndPassword(request.email(), request.password());
        if (member.isEmpty()) {
            throw new AuthorizationException("등록되지 않은 사용자입니다.");
        }

        return member.get();
    }
}
