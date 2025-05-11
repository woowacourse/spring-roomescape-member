package roomescape.domain.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.member.dto.request.MemberRequestDto;
import roomescape.domain.member.dto.response.MemberResponseDto;
import roomescape.domain.member.dto.response.MemberResponseDtoOfNames;
import roomescape.domain.member.model.Member;
import roomescape.domain.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        Member member = Member.createUser(memberRequestDto.name(), memberRequestDto.email(),
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

    public List<MemberResponseDtoOfNames> getAllMembers() {
        return memberRepository.findAll().stream()
            .map(MemberResponseDtoOfNames::from)
            .toList();
    }

}
