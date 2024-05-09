package roomescape.infrastructure.authentication;

import java.util.Base64;
import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.service.auth.AuthService;
import roomescape.service.auth.AuthenticatedMemberInfo;
import roomescape.service.auth.AuthenticationRequest;
import roomescape.service.auth.UnauthorizedException;

// TODO: 정리 필요
@Component
class SimpleAuthService implements AuthService {

    private final Member admin = new Member(
            new Name("admin"),
            "admin@woowacourse.com",
            "password"
    );

    @Override
    public String authenticate(AuthenticationRequest request) {
        String email = admin.getEmail();
        String password = admin.getPassword();
        AuthenticationRequest onlyMemberData = new AuthenticationRequest(email, password);

        if (!request.equals(onlyMemberData)) {
            throw new IllegalArgumentException("올바른 인증 정보를 입력해주세요.");
        }

        return generateToken(request);
    }

    private String generateToken(AuthenticationRequest request) {
        String base = request.email() + ":" + request.password();

        return Base64.getEncoder().encodeToString(base.getBytes());
    }

    @Override
    public AuthenticatedMemberInfo authorize(String token) {
        String decoded = new String(Base64.getDecoder().decode(token));
        String[] decodedStrings = decoded.split(":");
        if (decodedStrings.length < 2) {
            throw new UnauthorizedException();
        }
        String email = admin.getEmail();
        String password = admin.getPassword();
        AuthenticationRequest onlyMemberData = new AuthenticationRequest(email, password);
        AuthenticationRequest target = new AuthenticationRequest(decodedStrings[0], decodedStrings[1]);

        if (onlyMemberData.equals(target)) {
            return new AuthenticatedMemberInfo(admin.getName().value());
        }

        throw new UnauthorizedException();
    }
}
