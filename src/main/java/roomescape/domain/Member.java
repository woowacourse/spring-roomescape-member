package roomescape.domain;

import io.jsonwebtoken.Claims;
import java.util.Objects;

public class Member {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;

    public Member(final Long id, final String name, final String email, final String password, final String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Member createMemberWithoutPassword(Claims claims) {
        return new Member(
                Long.valueOf(claims.getSubject()),
                claims.get("name", String.class),
                claims.get("email", String.class),
                "0000",
                claims.get("role", String.class));
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public boolean isNotAdmin() {
        return !role.equals("ADMIN");
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
