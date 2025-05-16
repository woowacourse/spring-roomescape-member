package roomescape.global.auth.service;

import org.springframework.stereotype.Component;
import roomescape.global.auth.dto.LoginRequest;
import roomescape.global.auth.dto.LoginResponse;
import roomescape.global.auth.dto.UserInfo;
import roomescape.global.auth.exception.UnAuthorizedException;
import roomescape.global.auth.infrastructure.JwtProvider;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;
import roomescape.member.service.PasswordEncoder;

@Component
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(final JwtProvider jwtProvider, final MemberRepository memberRepository,
                       final PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponse login(final LoginRequest loginRequest) {
        Member member = checkEmailAndPassword(loginRequest.email(), loginRequest.password());
        String accessToken = jwtProvider.createToken(member);
        return new LoginResponse(accessToken);
    }

    public UserInfo makeUserInfo(final String token) {
        validateToken(token);
        Long memberId = jwtProvider.getMemberId(token);
        return new UserInfo(memberId, jwtProvider.getRole(token));
    }

    public Member existsMemberById(final UserInfo userInfo) {
        return memberRepository.findById(userInfo.id());
    }

    public Member checkEmailAndPassword(final String email, final String password) {
        Member member = findMemberByEmail(email);
        checkPassword(password, member);
        return member;
    }

    private void validateToken(final String token) {
        if (jwtProvider.isInvalidToken(token)) {
            throw new UnAuthorizedException("유효하지 않은 토큰입니다.");
        }
    }

    private Member findMemberByEmail(final String email) {
        return memberRepository.findMemberByEmail(email)
                .orElseThrow(() -> new UnAuthorizedException("존재하지 않은 사용자입니다."));
    }

    private void checkPassword(final String password, final Member member) {
        if (!passwordEncoder.matches(password, member.getPassword())) {
            System.out.println(password);
            System.out.println(passwordEncoder.encode(password));
            System.out.println(member.getPassword());
            throw new UnAuthorizedException("로그인에 실패하였습니다.");
        }
    }
}
