package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.TokenProvider;
import roomescape.auth.dto.response.LoginResponse;
import roomescape.member.domain.Member;
import roomescape.auth.dto.request.LoginRequest;
import roomescape.member.repository.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    public AuthService(final MemberRepository memberRepository, final TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    public LoginResponse login(final LoginRequest loginMemberRequest) {
        String email = loginMemberRequest.email();
        checkInvalidAuthInfo(email, loginMemberRequest.password());
        return new LoginResponse(tokenProvider.createToken(email));
    }

    private void checkInvalidAuthInfo(final String email, final String password) {
        Member member = findUser(email);
        checkInvalidPassword(member, password);
    }

    private Member findUser(final String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("로그인하려는 회원이 존재하지 않는 회원입니다."));
    }

    private void checkInvalidPassword(final Member member, final String password) {
        if (member.hasNotSamePassword(password)) {
            throw new IllegalArgumentException("로그인하려는 계정의 비밀번호가 올바르지 않습니다.");
        }
    }
}
