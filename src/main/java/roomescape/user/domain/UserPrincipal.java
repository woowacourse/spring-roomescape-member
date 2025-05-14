package roomescape.user.domain;

import java.util.List;

public record UserPrincipal(Long id, String name, String email, List<Role> roles) {

    public User toEntity() {
        return new User(
                id,
                name,
                email,
                null,
                roles
        );
    }
}
