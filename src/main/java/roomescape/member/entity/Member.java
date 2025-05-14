package roomescape.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Member {

    private final Long id;
    private String name;
    private String email;
    private String password;
    private RoleType role;

    public boolean matchesPassword(String password) {
        return this.password.equals(password);
    }
}
