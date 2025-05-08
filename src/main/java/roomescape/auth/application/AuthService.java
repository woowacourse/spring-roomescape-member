package roomescape.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.domain.AuthTokenProvider;
import roomescape.auth.ui.dto.CreateAccessTokenRequest;
import roomescape.exception.AuthenticationException;
import roomescape.exception.ResourceNotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.member.domain.UserRole;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthTokenProvider authTokenProvider;
    private final MemberRepository memberRepository;

    public String createAccessToken(final CreateAccessTokenRequest request) {
        final Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("해당 이메일을 가진 사용자가 존재하지 않습니다."));

        if (member.isWrongPassword(request.password())) {
            throw new AuthenticationException("이메일 혹은 비밀번호가 올바르지 않습니다.");
        }

        return authTokenProvider.createAccessToken(request.email(), member.getRole());
    }

    public Member getMemberByEmail(final String email) {
        return memberRepository.findByEmail(email)
                .orElseGet(() -> new Member("게스트", "게스트는_이메일이_없습니다", "패스워드도_없습니다", UserRole.GUEST));
    }
}
