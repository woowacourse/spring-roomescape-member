package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.domain.MemberRepository;
import roomescape.dto.request.MemberLoginRequest;
import roomescape.exception.NoSuchRecordException;
import roomescape.exception.WrongPasswordException;
import roomescape.infrastructure.JwtTokenProvider;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public String login(MemberLoginRequest loginRequest) {
        Member findMember = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new NoSuchRecordException("이메일: " + loginRequest.email() + " 해당하는 멤버를 찾을 수 없습니다"));

        if (!findMember.getPassword().equals(loginRequest.password())) {
            throw new WrongPasswordException("비밀번호가 틀렸습니다");
        }

        return jwtTokenProvider.createToken(findMember);
    }
}
