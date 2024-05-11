package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.service.dto.LoginRequest;
import roomescape.service.dto.MemberResponse;

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

        // TODO: 다른 사이트에서는 어떤 메시지를 내려주는지 확인
        if (member.isEmpty()) {
            System.out.println("없어");
            throw new IllegalArgumentException("해당 아이디와 비밀번호의 회원이 존재하지 않습니다.");
        }

        return member.get();
    }

    public MemberResponse findMemberById(Long memberId) {
        Optional<Member> member = memberRepository.findMemberById(memberId);

        if (member.isEmpty()) {
            throw new IllegalArgumentException("해당 회원이 존재하지 않습니다.");
        }

        return new MemberResponse(member.get().getName().getValue());
    }
}
