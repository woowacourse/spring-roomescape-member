package roomescape.service.auth;

import org.springframework.stereotype.Service;
import roomescape.controller.login.LoginRequest;
import roomescape.controller.login.LoginResponse;
import roomescape.controller.login.MemberResponse;
import roomescape.domain.Member;
import roomescape.exception.UnauthorizedException;
import roomescape.repository.MemberRepository;
import roomescape.service.exception.MemberNotFoundException;

import java.util.Optional;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public AuthService(final JwtTokenProvider jwtTokenProvider, final MemberRepository memberRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberRepository = memberRepository;
    }

    public boolean checkInvalidLogin(final String email, final String password) {
        final Optional<Member> member = memberRepository.findByEmail(email);
        return member.isPresent() && member.get().getPassword().equals(password);
    }

    public MemberResponse findMemberByEmail(final String email) {
        final Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 로그인 정보 입니다."));

        return MemberResponse.from(member);
    }

    public MemberResponse findMemberByToken(String token) {
        String payload = jwtTokenProvider.getPayload(token);

        return findMemberByEmail(payload);
    }

    public LoginResponse createToken(LoginRequest token) {
        if (checkInvalidLogin(token.email(), token.password())) {
            throw new UnauthorizedException("로그인 정보가 일치하지 않습니다.");
        }

        final String accessToken = jwtTokenProvider.createToken(token.email());
        return new LoginResponse(accessToken);
    }
}
