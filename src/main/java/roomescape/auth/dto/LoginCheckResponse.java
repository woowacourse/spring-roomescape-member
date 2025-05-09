package roomescape.auth.dto;

import roomescape.user.entity.User;

public class LoginCheckResponse {

    private final String name;

    private LoginCheckResponse(String name) {
        this.name = name;
    }

    public static LoginCheckResponse from(User user) {
        return new LoginCheckResponse(user.getName());
    }

    public String getName() {
        return name;
    }
}
