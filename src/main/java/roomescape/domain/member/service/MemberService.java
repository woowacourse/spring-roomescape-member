package roomescape.domain.member.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.member.domain.Member;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.global.exception.ClientIllegalArgumentException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findMemberById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        return member.orElseThrow(() -> new ClientIllegalArgumentException("없는 member를 조회 했습니다."));
    }

    public Member findMemberByEmailAndPassword(String email, String password) {
        Optional<Member> member = memberRepository.findByEmailAndPassword(email, password);
        return member.orElseThrow(() -> new ClientIllegalArgumentException("이메일 또는 비밀번호를 잘못 입력했습니다."));
    }
}
