package roomescape.domain.login.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.login.domain.Member;
import roomescape.domain.login.repository.MemberRepository;
import roomescape.global.exception.ClientIllegalArgumentException;

@Service
public class LoginService {

    private final MemberRepository memberRepository;

    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMemberById(Long id) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isEmpty()) {
            throw new ClientIllegalArgumentException("없는 member를 조회 했습니다.");
        }
        return member.get();
    }

    public Member findMemberByEmailAndPassword(String email, String password) {
        Optional<Member> member = memberRepository.findByEmailAndPassword(email, password);
        if (member.isEmpty()) {
            throw new ClientIllegalArgumentException("이메일 또는 비밀번호를 잘못 입력했습니다.");
        }
        return member.get();
    }
}
