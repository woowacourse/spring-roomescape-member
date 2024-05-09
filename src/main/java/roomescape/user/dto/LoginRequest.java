package roomescape.user.dto;

import roomescape.user.domain.User;

public record LoginRequest(String email, String password) {
    public User createUser() {
        return new User(email, password);
    }
}
