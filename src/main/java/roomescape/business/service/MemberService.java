package roomescape.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.business.Member;
import roomescape.persistence.MemberRepository;
import roomescape.presentation.dto.MemberRequestDto;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Long registerMember(MemberRequestDto memberRequestDto) {
        Member member = new Member(
                memberRequestDto.email(),
                memberRequestDto.password(),
                memberRequestDto.name()
        );
        return memberRepository.save(member);
    }
}
