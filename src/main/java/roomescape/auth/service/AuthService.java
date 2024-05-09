package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.dto.LoginCheckResponse;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.exception.InvalidTokenException;
import roomescape.exception.NoSuchRecordException;
import roomescape.exception.WrongPasswordException;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public String login(LoginRequest loginRequest) {
        Member findMember = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new NoSuchRecordException("이메일: " + loginRequest.email() + " 해당하는 멤버를 찾을 수 없습니다"));

        if (!findMember.getPassword().equals(loginRequest.password())) {
            throw new WrongPasswordException("비밀번호가 틀렸습니다");
        }

        return jwtTokenProvider.createToken(findMember);
    }

    public LoginCheckResponse checkLogin(String token) {
        if (jwtTokenProvider.isInvalidToken(token)) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다");
        }

        Long id = Long.valueOf(jwtTokenProvider.getPayload(token));
        Member findMember = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchRecordException("id: " + id + " 해당하는 회원을 찾을 수 없습니다"));

        return new LoginCheckResponse(findMember.getName());
    }
}
