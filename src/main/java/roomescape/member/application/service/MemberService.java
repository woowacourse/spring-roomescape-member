package roomescape.member.application.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.application.dto.CreateMemberRequest;
import roomescape.member.application.dto.GetMemberResponse;
import roomescape.member.application.repository.MemberRepository;
import roomescape.member.domain.Member;
import roomescape.member.presentation.dto.MemberNameResponse;
import roomescape.member.presentation.dto.RegisterRequest;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberNameResponse signup(RegisterRequest registerRequest) {
        CreateMemberRequest request = new CreateMemberRequest(
                registerRequest.name(),
                registerRequest.email(),
                registerRequest.password(),
                registerRequest.role()
        );
        Member member = memberRepository.insert(request);
        return new MemberNameResponse(member.getName());
    }

    public List<GetMemberResponse> getMembers() {
        return memberRepository.findAll().stream()
                .map(member -> new GetMemberResponse(member.id(), member.name()))
                .toList();
    }
}
