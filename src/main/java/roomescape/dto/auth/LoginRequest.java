package roomescape.dto.auth;

import roomescape.entity.Member;

public record LoginRequest(
        long id,
        String name,
        String email,
        String password
) {

    public static LoginRequest from(final Member member) {
        return new LoginRequest(member.getId(), member.getName(), member.getEmail(), member.getPassword());
    }
}
