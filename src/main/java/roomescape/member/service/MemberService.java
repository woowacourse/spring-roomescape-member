package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.exception.InvalidMemberException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.service.dto.SignUpRequest;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::new)
                .toList();
    }

    public Member findById(long id) {
        return memberRepository.getById(id);
    }
}
