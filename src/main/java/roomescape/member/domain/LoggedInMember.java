package roomescape.member.domain;

import java.util.Objects;

public class LoggedInMember {
    private final Long id;
    private final MemberName name;
    private final String email;
    private final MemberRole role;

    public LoggedInMember(Member member) {
        this(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public LoggedInMember(Long id, String name, String email, MemberRole role) {
        this.id = Objects.requireNonNull(id);
        this.name = new MemberName(name);
        this.email = Objects.requireNonNull(email);
        this.role = Objects.requireNonNull(role);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public boolean isAdmin() {
        return role.isAdmin();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        LoggedInMember that = (LoggedInMember) object;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(email, that.email)
                && role == that.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, role);
    }
}
