package roomescape.login.service;

import org.springframework.stereotype.Service;
import roomescape.common.authorization.JwtTokenProvider;
import roomescape.common.exception.AuthenticationException;
import roomescape.common.exception.InvalidEmailException;
import roomescape.login.dao.MemberDao;
import roomescape.login.domain.Member;
import roomescape.login.dto.MemberRequest;
import roomescape.login.dto.MemberResponse;
import roomescape.login.dto.TokenResponse;

@Service
public class LoginService {
    private static final String AUTHENTICATION_FAIL_EXCEPTION_MESSAGE = "회원 로그인에 실패했습니다";
    private static final String INVALID_EMAIL_EXCEPTION_MESSAGE = "존재하지 않는 이메일입니다";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public LoginService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(final MemberRequest memberRequest) {
        Member member = findMemberByEmail(memberRequest.email());
        validatePassword(member.getEmail(), memberRequest.password());

        String accessToken = jwtTokenProvider.createToken(memberRequest);
        return new TokenResponse(accessToken);
    }

    public MemberResponse findByToken(final String token) {
        String email = jwtTokenProvider.getPayload(token);
        return findByEmail(email);
    }

    public MemberResponse findByEmail(final String email) {
        Member member = findMemberByEmail(email);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }

    private Member findMemberByEmail(final String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new InvalidEmailException(INVALID_EMAIL_EXCEPTION_MESSAGE));
    }

    private void validatePassword(String email, String password) {
        if (!memberDao.isPasswordMatch(email, password)) {
            throw new AuthenticationException(AUTHENTICATION_FAIL_EXCEPTION_MESSAGE);
        }
    }
}
