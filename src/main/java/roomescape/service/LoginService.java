package roomescape.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.Member;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.dto.member.TokenResponse;
import roomescape.exception.InvalidAuthorizationException;
import roomescape.repository.MemberRepository;
import roomescape.util.JwtTokenProvider;

@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider) {
        this.memberRepository = memberRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(LoginRequest loginRequest) {
        if (checkInvalidLogin(loginRequest.email(), loginRequest.password())) {
            throw new InvalidAuthorizationException("[ERROR] 로그인 정보를 다시 확인해 주세요.");
        }

        String token = jwtTokenProvider.createToken(loginRequest.email());
        return new TokenResponse(token);
    }

    private boolean checkInvalidLogin(String email, String password) {
        Optional<Member> member = memberRepository.findByEmailAndPassword(email, password);
        return member.map(value -> !email.equals(value.getEmail()) || !password.equals(value.getPassword()))
                .orElse(true);
    }

    public MemberResponse findMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        return member.map(value -> new MemberResponse(value.getEmail()))
                .orElseThrow(() -> new InvalidAuthorizationException("[ERROR] 유효하지 않은 가입 정보입니다."));
    }

    public MemberResponse findMemberByToken(String token) {
        if (token == null || token.isBlank()) throw new InvalidAuthorizationException("[ERROR] 로그인이 필요합니다.");
        if (!jwtTokenProvider.validateToken(token)) throw new InvalidAuthorizationException("[ERROR] 로그인 상태가 만료되었습니다.");
        String payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }
}
