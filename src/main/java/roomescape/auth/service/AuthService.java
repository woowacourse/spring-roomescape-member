package roomescape.auth.service;

import org.springframework.stereotype.Service;
import roomescape.auth.jwt.AuthTokenExtractor;
import roomescape.auth.jwt.AuthTokenProvider;
import roomescape.auth.service.dto.CreateTokenServiceRequest;
import roomescape.exception.custom.AuthenticationException;
import roomescape.exception.custom.AuthorizationException;
import roomescape.exception.custom.NotExistedValueException;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final AuthTokenProvider authTokenProvider;
    private final AuthTokenExtractor authTokenExtractor;

    public AuthService(final MemberDao memberDao,
                       final AuthTokenProvider authTokenProvider,
                       final AuthTokenExtractor authTokenExtractor) {
        this.memberDao = memberDao;
        this.authTokenProvider = authTokenProvider;
        this.authTokenExtractor = authTokenExtractor;
    }

    public String createToken(final CreateTokenServiceRequest request) {
        final Member member = memberDao.findByEmail(request.email())
                .orElseThrow(() -> new AuthenticationException("존재하지 않는 이메일 입니다"));

        member.validateRightPassword(request.password());

        return authTokenProvider.createTokenFromMember(member);
    }

    public Member findMemberByToken(final String token) {
        validateTokenExisted(token);
        final long memberId = Long.parseLong(authTokenExtractor.extractMemberIdFromToken(token));

        return memberDao.findById(memberId)
                .orElseThrow(() -> new NotExistedValueException("존재하지 않는 멤버 입니다"));
    }

    private void validateTokenExisted(final String token) {
        if (token == null || token.isBlank()) {
            throw new AuthorizationException("로그인 토큰이 존재하지 않습니다");
        }
    }

    public boolean isAdmin(final String token) {
        return authTokenExtractor.extractMemberRoleFromToken(token).equals("ADMIN");
    }
}
