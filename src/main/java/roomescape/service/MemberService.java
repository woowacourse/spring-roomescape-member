package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.auth.MemberNameResponseDto;
import roomescape.exception.UnauthorizationException;
import roomescape.repository.MemberRepository;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMemberById(long id) {
        return memberRepository.findById(id).orElseThrow(() -> new UnauthorizationException("[ERROR] 유저를 찾을 수 없습니다. ID : " + id));
    }

    public List<MemberNameResponseDto> findAllMembers() {
        return memberRepository.findAll().stream()
                .map(member -> new MemberNameResponseDto(member.getName()))
                .toList();
    }
}
