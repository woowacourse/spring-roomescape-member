package roomescape.service.member;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.member.MemberRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.repository.member.MemberRepository;

@Service
public class MemberDefaultService implements MemberService {
    private final MemberRepository memberRepository;

    public MemberDefaultService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public MemberResponse create(MemberRequest request) {
        Member member = Member.createWithoutId(request.name(), request.email(), request.password());
        return MemberResponse.from(memberRepository.add(member));
    }
}
