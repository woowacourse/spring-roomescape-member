package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.exception.UnauthorizationException;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findLoginMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UnauthorizationException("[ERROR] 유저를 찾을 수 없습니다. ID : " + id));
    }
}
