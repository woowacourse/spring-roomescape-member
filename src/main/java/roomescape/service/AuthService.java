package roomescape.service;

import java.util.Map;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.domain.Role;
import roomescape.exception.AuthenticationException;
import roomescape.service.dto.LoginMember;
import roomescape.service.dto.MemberResponse;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(MemberResponse memberResponse) {
        Map<String, Object> payload = Map.of(
                "sub", String.valueOf(memberResponse.id()),
                "name", memberResponse.name(),
                "role", memberResponse.role()
        );

        return jwtTokenProvider.createToken(payload);
    }

    public LoginMember findMemberByToken(String token) {
        validateToken(token);
        Map<String, Object> payload = jwtTokenProvider.getPayload(token);

        return new LoginMember(
                Long.parseLong((String) payload.get("sub")),
                (String) payload.get("name"),
                Role.valueOf((String) payload.get("role"))
        );
    }

    public void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }
    }
}
