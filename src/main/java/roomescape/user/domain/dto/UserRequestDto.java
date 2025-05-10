package roomescape.user.domain.dto;

import roomescape.user.domain.User;

public record UserRequestDto(String name, String role, String email, String password) {

    public User toEntity() {
        return new User(name, role, email, password);
    }
}
