package roomescape.auth.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@Getter
@RequiredArgsConstructor
public class AuthInfo {
    private final Long id;
    private final String name;
    private final String email;
    private final Role role;

    public static AuthInfo of(Member member) {
        return new AuthInfo(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
