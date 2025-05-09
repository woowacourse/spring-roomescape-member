package roomescape.dto.request;

import roomescape.domain.Member;

public record LoginMember(
    Long id,
    String name,
    String email,
    String password
) {

    public static LoginMember from(Member user) {
        return new LoginMember(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPassword()
        );
    }
}
