package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.persistence.MemberRepository;
import roomescape.service.response.MemberResponse;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }
}
