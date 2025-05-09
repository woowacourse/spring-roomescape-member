package roomescape.dto.request;

import roomescape.domain.Member;
import roomescape.domain.Role;

public record LoginMember(
    Long id,
    String name,
    String email,
    String password,
    Role role
) {

    public static LoginMember from(Member user) {
        return new LoginMember(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword(),
            user.getRole()
        );
    }
}
