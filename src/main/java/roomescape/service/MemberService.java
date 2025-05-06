package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.exception.InvalidMemberException;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findByEmailAndPassword(LoginRequest request) {
        return memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new InvalidMemberException("유효하지 않은 로그인 정보입니다."));
    }
}
