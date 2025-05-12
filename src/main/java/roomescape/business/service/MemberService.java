package roomescape.business.service;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.business.Member;
import roomescape.business.MemberRole;
import roomescape.persistence.MemberRepository;
import roomescape.presentation.dto.SignUpRequestDto;
import roomescape.presentation.dto.response.MemberResponseDto;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponseDto> readMemberAll() {
        List<Member> findMembers = memberRepository.findAll();
        return findMembers.stream()
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        member.getName(),
                        member.getEmail()
                ))
                .toList();
    }

    public MemberResponseDto createMember(SignUpRequestDto signUpRequestDto) {
        Member member = new Member(
                signUpRequestDto.name(),
                signUpRequestDto.email(),
                signUpRequestDto.password(),
                MemberRole.USER
        );
        Long memberId = memberRepository.add(member);
        return new MemberResponseDto(memberId, member.getName(), member.getEmail());
    }
}
