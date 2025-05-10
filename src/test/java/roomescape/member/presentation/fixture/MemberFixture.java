package roomescape.member.presentation.fixture;

import roomescape.auth.presentation.dto.LoginRequest;
import roomescape.member.domain.Role;
import roomescape.member.presentation.dto.RegisterRequest;

public class MemberFixture {

    public LoginRequest loginRequest(String email, String password) {
        return new LoginRequest(email, password);
    }

    public RegisterRequest registerRequest(String name, String email, String password, Role role) {
        return new RegisterRequest(email, password, name, role);
    }
}
