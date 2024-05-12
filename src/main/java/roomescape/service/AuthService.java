package roomescape.service;

import java.util.Objects;
import java.util.Optional;

import jakarta.servlet.http.Cookie;

import org.springframework.stereotype.Service;

import roomescape.controller.rest.request.LoginRequest;
import roomescape.domain.Member;
import roomescape.exception.AuthenticationException;
import roomescape.exception.EntityNotFoundException;
import roomescape.repository.MemberDao;
import roomescape.util.JwtProvider;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    public AuthService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public Member findByEmail(String email) {
        Optional<Member> member = memberDao.findByEmail(email);
        if (member.isEmpty()) {
            throw new EntityNotFoundException("Member with email %s not found.".formatted(email));
        }
        return member.get();
    }

    public Member findByToken(String token) {
        String email = jwtProvider.getSubject(token);
        return findByEmail(email);
    }

    public Cookie createToken(LoginRequest request) {
        validate(request);
        String token = jwtProvider.createToken(request.email());
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    private void validate(LoginRequest request) {
        Member member = findByEmail(request.email());
        if (!isValidLogin(request, member)) {
            throw new AuthenticationException("Invalid password.");
        }
    }

    private boolean isValidLogin(LoginRequest request, Member member) {
        return Objects.equals(member.email(), request.email())
                && Objects.equals(member.password(), request.password());
    }
}
