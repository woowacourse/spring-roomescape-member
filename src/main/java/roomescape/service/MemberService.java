package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.repository.user.MemberRepository;
import roomescape.service.dto.MemberResponse;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> readMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }
}
