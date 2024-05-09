package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.domain.SiteUser;
import roomescape.dto.LogInRequest;
import roomescape.infrastructure.JdbcSiteUserRepository;
import roomescape.service.exception.ResourceNotFoundException;

import java.security.Key;

@Service
public class SiteUserService {

    private static final String JWT_SECRET_KEY = "42616SDFKJ156S39487DF45U8OSDFK13209809NUWSC64756E67";

    private final JdbcSiteUserRepository jdbcSiteUserRepository;

    public SiteUserService(JdbcSiteUserRepository jdbcSiteUserRepository) {
        this.jdbcSiteUserRepository = jdbcSiteUserRepository;
    }

    public String logIn(LogInRequest logInRequest) {
        String email = logInRequest.email();
        String password = logInRequest.password();

        SiteUser siteUser = jdbcSiteUserRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ResourceNotFoundException("일치하는 이메일과 비밀번호가 없습니다."));

        byte[] bytes = JWT_SECRET_KEY.getBytes();
        Key key = Keys.hmacShaKeyFor(bytes);

        return Jwts.builder()
                .setSubject(siteUser.getEmail())
                .claim("name", siteUser.getName())
                .claim("password", siteUser.getPassword())
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }
}
