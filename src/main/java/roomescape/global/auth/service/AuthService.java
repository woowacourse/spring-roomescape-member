package roomescape.global.auth.service;

import org.springframework.stereotype.Component;
import roomescape.global.auth.dto.LoginRequest;
import roomescape.global.auth.dto.LoginResponse;
import roomescape.global.auth.dto.UserInfo;
import roomescape.global.auth.exception.AuthException;
import roomescape.global.auth.exception.UnAuthorizedException;
import roomescape.global.auth.infrastructure.JwtProvider;
import roomescape.member.domain.MemberRole;
import roomescape.member.repository.MemberRepository;

@Component
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public AuthService(final JwtProvider jwtProvider, final MemberRepository memberRepository) {
        this.jwtProvider = jwtProvider;
        this.memberRepository = memberRepository;
    }

    public LoginResponse createToken(final LoginRequest loginRequest) {
        UserInfo userInfo = getUserInfoByEmailAndPassword(loginRequest.email(), loginRequest.password());
        String accessToken = jwtProvider.createToken(userInfo);
        return new LoginResponse(accessToken);
    }

    public UserInfo makeUserInfo(final String token, final MemberRole memberRole) {
        validateToken(token);
        Long memberId = jwtProvider.getMemberId(token);
        validateMember(memberId, memberRole);
        return new UserInfo(memberId, jwtProvider.getName(token), jwtProvider.getRole(token));
    }

    private void validateToken(final String token) {
        if (jwtProvider.isInvalidToken(token)) {
            throw new AuthException("유효하지 않은 토큰입니다.");
        }
    }

    private UserInfo getUserInfoByEmailAndPassword(final String email, final String password) {
        return memberRepository.findMemberByEmailAndPassword(email, password)
                .orElseThrow(() -> new AuthException("존재하지 않은 사용자입니다."));
    }

    private void validateMember(final Long id, final MemberRole memberRole) {
        if (memberRole == MemberRole.USER) {
            validateExistsById(id);
            return;
        }
        validateExistsByIdAndMemberRole(id, memberRole);
    }

    private void validateExistsById(final Long id) {
        if (!memberRepository.existsById(id)) {
            throw new AuthException("존재하지 않은 사용자입니다.");
        }
    }

    private void validateExistsByIdAndMemberRole(final Long id, final MemberRole memberRole) {
        if (!memberRepository.existsByIdAndMemberRole(id, memberRole)) {
            throw new UnAuthorizedException("접근 권한을 벗어났습니다.");
        }
    }
}
