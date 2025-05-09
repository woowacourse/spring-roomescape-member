package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.member.presentation.MemberResponse;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findByEmail(final String email) {
        return memberRepository.findByEmail(email);
    }

    public boolean isExistsByEmail(final String email) {
        return memberRepository.isExistsByEmail(email);
    }

    public Member findById(final Long id) {
        return memberRepository.findById(id);
    }

    public List<MemberResponse> findAll() {
        return memberRepository.findAll().stream()
                .map(member -> new MemberResponse(member.getId(), member.getName()))
                .toList();
    }
}
