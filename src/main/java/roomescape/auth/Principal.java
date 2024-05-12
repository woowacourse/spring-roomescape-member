package roomescape.auth;

import roomescape.domain.member.Member;
import roomescape.domain.member.Role;

public class Principal {
    private final long id;
    private final String name;
    private final Role role;

    private Principal(long id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public static Principal from(Member member) {
        return new Principal(member.getId(), member.getName(), member.getRole());
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }
}
