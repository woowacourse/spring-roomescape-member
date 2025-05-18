package roomescape.business.service;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.util.List;
import roomescape.business.Member;
import roomescape.business.MemberRole;
import roomescape.exception.MemberException;
import roomescape.persistence.MemberRepository;
import roomescape.presentation.dto.SignUpRequestDto;
import roomescape.presentation.dto.response.MemberResponseDto;

@Named
public class MemberService {

    private final MemberRepository memberRepository;

    @Inject
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
        if (memberRepository.existsByEmail(signUpRequestDto.email())) {
            throw new MemberException("이미 등록된 사용자입니다.");
        }
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
