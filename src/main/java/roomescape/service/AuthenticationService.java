package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.dto.LoginRequest;
import roomescape.repository.JdbcMemberRepository;

@Service
public class AuthenticationService {

    @Value("${secret.key}")
    private String SECRET_KEY;

    private final JdbcMemberRepository jdbcMemberRepository;

    public AuthenticationService(JdbcMemberRepository jdbcMemberRepository) {
        this.jdbcMemberRepository = jdbcMemberRepository;
    }


    public String generateToken(LoginRequest loginRequest) {
        Member member = jdbcMemberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s, 이메일과 일치하는 멤버가 존재하지 않습니다.", loginRequest.email()))
                );

        if (!member.getPassword().equals(loginRequest.password())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Long findByToken(String token) {
        return Long.valueOf(
                Jwts.parser()
                        .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }
}
