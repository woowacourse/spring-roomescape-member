package roomescape.service;

import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import roomescape.dao.MemberDao;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberPassword;
import roomescape.dto.member.MemberLoginRequest;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.service.exception.AuthorizationException;
import roomescape.service.exception.InvalidRequestException;

@Service
public class AuthService {

    private static final String COOKIE_NAME = "token";

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(MemberLoginRequest request) {
        checkInvalidLogin(request);
        return jwtTokenProvider.createToken(findMember(request));
    }

    private Member findMember(MemberLoginRequest request) {
        return memberDao.readByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 사용자입니다."));
    }

    private Member findMember(Long id) {
        return memberDao.readById(id)
                .orElseThrow(() -> new InvalidRequestException("존재하지 않는 사용자입니다."));
    }

    public Member findMemberByToken(String token) {
        Long payload = jwtTokenProvider.getPayload(token);
        return findMember(payload);
    }

    public boolean isAdminByToken(String token) {
        return findMemberByToken(token).isAdmin();
    }

    public Cookie makeAuthCookie(String token) {
        return new Cookie(COOKIE_NAME, token);
    }

    public Cookie cleanAuthCookie() {
        return new Cookie(COOKIE_NAME, null);
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        validateCookieExist(cookies);
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(COOKIE_NAME))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(() -> new AuthorizationException("로그인이 필요합니다."));
    }

    private void checkInvalidLogin(MemberLoginRequest request) {
        Member member = memberDao.readByEmail(request.email())
                .orElseThrow(() -> new AuthorizationException("아이디와 비밀번호를 확인해주세요."));
        if (member.isNotSame(new MemberPassword(request.password()))) {
            throw new AuthorizationException("아이디와 비밀번호를 확인해주세요.");
        }
    }

    private static void validateCookieExist(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            throw new AuthorizationException("로그인이 필요합니다.");
        }
    }
}
