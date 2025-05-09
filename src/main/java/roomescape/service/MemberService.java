package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.request.MemberRequestDto;
import roomescape.dto.response.MemberResponseDto;
import roomescape.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        Member member = new Member(memberRequestDto.name(), memberRequestDto.email(),
            memberRequestDto.password());
        memberRepository.save(member);
        return MemberResponseDto.from(member);
    }

    public Member getMemberOf(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password);
    }

    public Member getMemberFrom(String email) {
        return memberRepository.findByEmail(email);
    }
}
