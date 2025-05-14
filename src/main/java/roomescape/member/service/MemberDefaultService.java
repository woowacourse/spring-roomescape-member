package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberDefaultService {
    private final MemberRepository memberRepository;

    public MemberDefaultService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse create(MemberRequest request) {
        Member member = Member.createWithoutId(request.name(), request.email(), request.password());
        return MemberResponse.from(memberRepository.add(member));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public List<MemberResponse> getAll() {
        return MemberResponse.from(memberRepository.findAll());
    }
}
