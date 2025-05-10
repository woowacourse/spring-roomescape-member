package roomescape.member.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.member.service.dto.response.MemberServiceResponse;

@Service
@RequiredArgsConstructor
public class AdminMemberService {

    private final MemberRepository memberRepository;

    public List<MemberServiceResponse> getAll() {
        List<Member> members = memberRepository.getAll();
        return members.stream()
                .map(MemberServiceResponse::from)
                .toList();
    }
}
