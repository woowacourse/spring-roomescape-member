package roomescape.util.fixture;

import roomescape.auth.dto.TokenRequest;
import roomescape.auth.service.AuthService;

public class AuthFixture {

    public static final String USER_EMAIL = "user@user.com";
    public static final String USER_PASSWORD = "user123";

    public static final String ADMIN_EMAIL = "admin@admin.com";
    public static final String ADMIN_PASSWORD = "admin123";

    public static String createUserToken(AuthService service) {
        TokenRequest request = new TokenRequest(USER_EMAIL, USER_PASSWORD);
        return service.createToken(request);
    }

    public static String createAdminToken(AuthService service) {
        TokenRequest request = new TokenRequest(ADMIN_EMAIL, ADMIN_PASSWORD);
        return service.createToken(request);
    }

}
