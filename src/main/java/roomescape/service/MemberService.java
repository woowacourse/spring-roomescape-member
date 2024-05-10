package roomescape.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Optional;
import org.springframework.stereotype.Service;
import roomescape.domain.member.Email;
import roomescape.domain.member.Member;
import roomescape.domain.member.Password;
import roomescape.dto.request.LoginRequest;
import roomescape.exceptions.ValidationException;
import roomescape.repository.member.MemberRepository;


@Service
public class MemberService {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E";
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String login(LoginRequest loginRequest) {
        Optional<Member> optionalMember = memberRepository.findByEmailAndPassword(
                new Email(loginRequest.email()),
                new Password(loginRequest.password())
        );
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            String accessToken = Jwts.builder()
                    .setSubject(member.getId().toString())
                    .claim("name", member.getName())
                    .claim("role", member.getRole().getValue())
                    .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .compact();
            return accessToken;
        }
        throw new ValidationException("이메일 또는 비밀번호가 틀립니다.");
    }
}
