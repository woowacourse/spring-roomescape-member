package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.MemberRepository;
import roomescape.service.dto.MemberResponse;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findAllMembers() {
        return memberRepository.findAllMembers()
                .stream()
                .map(MemberResponse::new)
                .toList();
    }
}
