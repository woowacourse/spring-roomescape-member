package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.dto.response.LoginResponse;
import roomescape.entity.LoginMember;
import roomescape.exception.AuthenticationException;
import roomescape.repository.MemberDao;


@Service
public class MemberService {

    public static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public LoginResponse login(LoginRequest request) {
        LoginMember loginMember = memberDao.findByEmailAndPassword(request.email(), request.password())
            .orElseThrow(() -> new AuthenticationException("로그인 정보를 찾을 수 없습니다."));

        String accessToken = Jwts.builder()
            .setSubject(loginMember.getId().toString())
            .claim("name", loginMember.getName())
            .claim("role", loginMember.getRole())
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .compact();

        return LoginResponse.from(accessToken);
    }

    public LoginCheckResponse findByToken(String token) {
        Long memberId = Long.valueOf(Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody().getSubject());

        LoginMember findLoginMember = memberDao.findById(memberId)
            .orElseThrow(() -> new AuthenticationException("로그인 정보가 일치하지 않습니다."));
        return LoginCheckResponse.of(findLoginMember.getName());
    }
}
