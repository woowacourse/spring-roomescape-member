package roomescape.login.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.naming.AuthenticationException;
import org.springframework.stereotype.Service;
import roomescape.login.dto.LoginRequest;
import roomescape.member.domain.Email;
import roomescape.member.domain.Member;
import roomescape.member.domain.Password;
import roomescape.member.repository.MemberRepository;

@Service
public class LoginService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";
    private final MemberRepository memberRepository;

    public LoginService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String createLoginToken(LoginRequest loginRequest) throws AuthenticationException {
        return memberRepository.findByEmailAndPassword(
                        new Email(loginRequest.email()),
                        new Password(loginRequest.password()))
                .map(this::parseToToken)
                .orElseThrow(() -> new AuthenticationException("이메일 또는 비밀번호가 틀립니다."));
    }

    private String parseToToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName().name())
                .claim("email", member.getEmail().email())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }
}
