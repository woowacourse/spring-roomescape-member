package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dto.MemberResponse;
import roomescape.dto.MemberResponses;
import roomescape.repository.MemberRepository;

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
}
