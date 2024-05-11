package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.MemberResponse;
import roomescape.repository.MemberRepository;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> getMembers() {
        return memberRepository.findAll()
                .stream()
                .map(MemberResponse::new)
                .toList();
    }
}
