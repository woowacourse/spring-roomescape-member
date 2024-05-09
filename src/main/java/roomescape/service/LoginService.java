package roomescape.service;

import static roomescape.exception.ExceptionType.NOT_FOUND_MEMBER;
import static roomescape.exception.ExceptionType.WRONG_PASSWORD;

import io.jsonwebtoken.Claims;
import java.util.Map;
import org.springframework.stereotype.Service;
import roomescape.domain.LoginMember;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;
import roomescape.exception.RoomescapeException;
import roomescape.repository.MemberRepository;

@Service
public class LoginService {

    private final MemberRepository memberRepository;
    private final JwtGenerator jwtGenerator;

    public LoginService(MemberRepository memberRepository, JwtGenerator jwtGenerator) {
        this.memberRepository = memberRepository;
        this.jwtGenerator = jwtGenerator;
    }

    public String getLoginToken(LoginRequest loginRequest) {
        Member findMember = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new RoomescapeException(NOT_FOUND_MEMBER));
        if (!findMember.getPassword().equals(loginRequest.password())) {
            throw new RoomescapeException(WRONG_PASSWORD);
        }

        return jwtGenerator.generateWith(Map.of(
                "id", findMember.getId(),
                "name", findMember.getName()
        ));
    }

    public LoginMember checkLogin(String token) {
        Claims claims = jwtGenerator.getClaims(token);
        return new LoginMember(
                claims.get("id", Long.class),
                claims.get("name", String.class)
        );
    }
}
