package roomescape.infrastructure.authentication;

import org.springframework.stereotype.Component;
import roomescape.domain.Member;
import roomescape.domain.Name;
import roomescape.service.AuthService;
import roomescape.service.request.AuthenticationRequest;

@Component
class SimpleAuthService implements AuthService {

    private final Member admin = new Member(
            new Name("admin"),
            "admin@woowacourse.com",
            "password"
    );

    @Override
    public boolean verify(AuthenticationRequest request) {
        String email = admin.getEmail();
        String password = admin.getPassword();
        AuthenticationRequest onlyMemberData = new AuthenticationRequest(email, password);

        return request.equals(onlyMemberData);
    }
}
