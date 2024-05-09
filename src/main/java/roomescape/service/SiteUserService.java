package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import roomescape.domain.SiteUser;
import roomescape.dto.LogInRequest;
import roomescape.dto.ProfileNameResponse;
import roomescape.infrastructure.JdbcSiteUserRepository;
import roomescape.service.exception.ResourceNotFoundException;

import java.security.Key;

@Service
public class SiteUserService {

    private static final Key KEY = Keys.hmacShaKeyFor("42616SDFKJ156S39487DF45U8OSDFK13209809NUWSC64756E67".getBytes());

    private final JdbcSiteUserRepository jdbcSiteUserRepository;

    public SiteUserService(JdbcSiteUserRepository jdbcSiteUserRepository) {
        this.jdbcSiteUserRepository = jdbcSiteUserRepository;
    }

    public String logIn(LogInRequest logInRequest) {
        String email = logInRequest.email();
        String password = logInRequest.password();

        SiteUser siteUser = jdbcSiteUserRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ResourceNotFoundException("일치하는 이메일과 비밀번호가 없습니다."));

        return Jwts.builder().subject(siteUser.getId().toString())
                .claim("email", siteUser.getEmail())
                .claim("name", siteUser.getName())
                .signWith(KEY)
                .compact();
    }

    public ProfileNameResponse getNameIfLogIn(String token) {
        Long memberId = Long.valueOf(
                Jwts.parser().setSigningKey(KEY).build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getSubject());
        SiteUser siteUser = findValidatedSiteUserById(memberId);

        return new ProfileNameResponse(siteUser.getName());
    }

    private SiteUser findValidatedSiteUserById(Long memberId) {
        return jdbcSiteUserRepository.findById(memberId).orElseThrow(
                () -> new ResourceNotFoundException("아이디에 해당하는 사용자가 없습니다."));
    }
}
