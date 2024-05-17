package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.MemberResponses;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponses findAll() {
        List<MemberResponse> memberResponses = memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
        return new MemberResponses(memberResponses);
    }

    public MemberResponse findByMemberName(String name) {
        Member member = memberRepository.findByMemberName(name);
        return MemberResponse.from(member);
    }
}
