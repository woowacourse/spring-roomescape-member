package roomescape.domain.auth.dto;

import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;

public record LoginUserDto(Long id, Roles role) {

    public static LoginUserDto from(final User user) {
        return new LoginUserDto(user.getId(), user.getRole());
    }
}
