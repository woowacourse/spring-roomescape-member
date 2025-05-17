package roomescape.business.service.member;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.persistence.MemberRepository;
import roomescape.presentation.member.dto.MemberResponseDto;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponseDto> getMembers() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponseDto::toResponse)
                .toList();
    }
}
