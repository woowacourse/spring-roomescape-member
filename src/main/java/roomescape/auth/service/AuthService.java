package roomescape.auth.service;

import java.util.function.Supplier;
import org.springframework.stereotype.Service;
import roomescape.auth.TokenProvider;
import roomescape.auth.dto.response.GetAuthInfoResponse;
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
        Member member = findMember(email, () -> "로그인하려는 계정이 존재하지 않습니다. 회원가입 후 로그인해주세요.");
        checkInvalidPassword(member, password);
    }

    private Member findMember(final String email, final Supplier<String> exceptionMessage) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(exceptionMessage.get()));
    }

    private void checkInvalidPassword(final Member member, final String password) {
        if (member.hasNotSamePassword(password)) {
            throw new IllegalArgumentException("아이디 또는 비밀번호를 잘못 입력했습니다. 다시 입력해주세요.");
        }
    }

    public GetAuthInfoResponse getMemberAuthInfo(final String token) {
        String email = tokenProvider.extractPayload(token);
        Member member = findMember(email, () -> "사용자 정보를 찾을 수 없습니다. 다시 로그인해주세요.");
        return GetAuthInfoResponse.from(member);
    }
}
