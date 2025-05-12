package roomescape.member.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.member.domain.Member;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(final MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<MemberResponse> getMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponse::from)
                .toList();
    }

    public Member getMember(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password)
                .orElseThrow(MemberNotFoundException::new);
    }

    public Member getMember(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    public void delete(long id) {
        if (!memberRepository.deleteById(id)) {
            throw new MemberNotFoundException();
        }
    }
}
