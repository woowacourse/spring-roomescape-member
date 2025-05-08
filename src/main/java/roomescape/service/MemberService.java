package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.repository.MemberRepository;
import roomescape.dto.response.MemberResponse;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findAllMembers() {
        return memberRepository.findAll().stream().map(MemberResponse::toDto).toList();
    }
}
