package roomescape.login.service;

import org.springframework.stereotype.Service;
import roomescape.common.authorization.JwtTokenProvider;
import roomescape.common.exception.InvalidEmailException;
import roomescape.login.dao.MemberDao;
import roomescape.login.domain.Member;
import roomescape.login.dto.MemberResponse;
import roomescape.login.dto.TokenRequest;
import roomescape.login.dto.TokenResponse;

@Service
public class LoginService {
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String INVALID_EMAIL_EXCEPTION_MESSAGE = "존재하지 않는 이메일입니다";
    public static final String INVALID_EMAIL_FORMAT_EXCEPTION_MESSAGE = "올바른 이메일 양식을 입력해주세요";

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public LoginService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public TokenResponse createToken(final TokenRequest tokenRequest) {
        validateEmail(tokenRequest.email());
        String accessToken = jwtTokenProvider.createToken(tokenRequest);
        return new TokenResponse(accessToken);
    }

    public MemberResponse findMemberByToken(final String token) {
        String email = jwtTokenProvider.getPayload(token);
        return findMemberByEmail(email);
    }

    public MemberResponse findMemberByEmail(final String email) {
        Member member = memberDao.findByEmail(email)
                .orElseThrow(() -> new InvalidEmailException(INVALID_EMAIL_EXCEPTION_MESSAGE));

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }

    private void validateEmail(final String principal) {
        if (!principal.matches(EMAIL_REGEX)) {
            throw new InvalidEmailException(INVALID_EMAIL_FORMAT_EXCEPTION_MESSAGE);
        }
    }
}
