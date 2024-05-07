package roomescape.controller.request;

import roomescape.domain.User;

public record UserSignUpRequest(String name, String email, String password) {
    public User toEntity() {
        return new User(name, email, password);
    }
}
