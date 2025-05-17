package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.controller.dto.SignupRequest;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> getAll() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::from)
                .toList();
    }

    public MemberResponse add(SignupRequest request) {
        Member member = request.toMemberWithoutId();
        Long id = memberRepository.saveAndReturnId(member);
        return MemberResponse.from(member.createMemberWithId(id));
    }

}
