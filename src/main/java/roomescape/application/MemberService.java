package roomescape.application;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.infrastructure.repository.MemberRepository;

import java.util.NoSuchElementException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() ->  new NoSuchElementException("[ERROR] 해당 이메일로 가입한 회원이 존재하지 않습니다."));
    }
}
