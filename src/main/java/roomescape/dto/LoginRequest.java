package roomescape.dto;

import roomescape.domain.User;

public record LoginRequest(String email, String password) {
    public User createUser() {
        return new User(email, password);
    }
}
