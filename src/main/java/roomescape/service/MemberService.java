package roomescape.service;

import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.persistence.MemberRepository;
import roomescape.service.request.MemberLoginRequest;
import roomescape.utils.JwtTokenProvider;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(MemberLoginRequest request) {
        Member findMember = memberRepository.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new NoSuchElementException("올바르지 않은 회원 정보입니다."));

        return jwtTokenProvider.createToken(findMember);
    }
}
