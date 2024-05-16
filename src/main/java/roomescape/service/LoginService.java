package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.service.dto.LoginRequest;

@Service
public class LoginService {

    private final MemberRepository memberRepository;

    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMemberByEmailAndPassword(LoginRequest request) {
        Optional<Member> member = memberRepository.findMemberByEmailAndPassword(
                request.getEmail(),
                request.getPassword());

        if (member.isEmpty()) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 잘못되었습니다.");
        }

        return member.get();
    }

    public Member findMemberById(Long memberId) {
        Optional<Member> member = memberRepository.findMemberById(memberId);

        if (member.isEmpty()) {
            throw new IllegalArgumentException("해당 회원이 존재하지 않습니다.");
        }

        return member.get();
    }
}
