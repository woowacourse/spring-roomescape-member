package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.dto.LoginRequest;
import roomescape.member.util.JwtTokenProvider;
import roomescape.member.util.TokenProvider;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public MemberService(MemberRepository memberRepository, TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public String checkLogin(LoginRequest loginRequest) {
        if (checkInvalidLogin(loginRequest.getEmail(), loginRequest.getPassword())) {
            throw new IllegalArgumentException("이메일 혹은 비밀번호가 일치하지 않습니다.");
        }

        return tokenProvider.createToken(loginRequest.getEmail());
    }

    private boolean checkInvalidLogin(String email, String password) {
        return !memberRepository.existBy(email, password);
    }
}
