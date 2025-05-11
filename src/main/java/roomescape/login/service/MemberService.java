package roomescape.login.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.exception.DataNotFoundException;
import roomescape.login.domain.Member;
import roomescape.login.repository.MemberRepository;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("해당 회원 데이터가 존재하지 않습니다. email = " + email));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }
}
