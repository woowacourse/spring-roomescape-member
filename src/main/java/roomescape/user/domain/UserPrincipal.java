package roomescape.user.domain;

import java.util.List;

public record UserPrincipal(Long id, String name, String email, List<Role> roles) {

    public static UserPrincipal from(User user) {
        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
