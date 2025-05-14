package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.model.Member;
import roomescape.repository.MemberRepository;

@Component
public class MemberFinder {
    private final MemberRepository memberRepository;

    public MemberFinder(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디 해당하는 멤버 없습니다"));
    }

}
