package roomescape.service;

import java.util.Objects;

import jakarta.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import roomescape.controller.rest.request.LoginRequest;
import roomescape.domain.Member;
import roomescape.exception.AuthenticationException;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.MemberDao;
import roomescape.util.CookieUtil;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final CookieUtil tokenUtil;

    public AuthService(MemberDao memberDao, CookieUtil tokenUtil) {
        this.memberDao = memberDao;
        this.tokenUtil = tokenUtil;
    }

    public Member findFromCookies(Cookie[] cookies) {
        String email = extractTokenValue(cookies);
        return findByEmail(email);
    }

    public Member findByEmail(String email) {
        return memberDao.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Member with email " + email + " not found."));
    }

    public Cookie createToken(LoginRequest request) {
        validate(request);
        return tokenUtil.create(request.email());
    }

    public Cookie expiredToken() {
        return tokenUtil.expired();
    }

    private String extractTokenValue(Cookie[] cookies) {
        return tokenUtil.extractValue(cookies)
                .orElseThrow(() -> new AuthenticationException("Token doesn't exist."));
    }

    private void validate(LoginRequest request) {
        Member member = findByEmail(request.email());
        if (!isValidLogin(request, member)) {
            throw new AuthenticationException("Invalid password.");
        }
    }

    private boolean isValidLogin(LoginRequest request, Member member) {
        return Objects.equals(member.getEmail(), request.email())
                && Objects.equals(member.getPassword(), request.password());
    }
}
