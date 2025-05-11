package roomescape.member.domain;

public record Visitor(
        Long id,
        String name,
        String email,
        Role role
) {

    public boolean isAdmin() {
        return role.getId() == 2L;
    }

    public boolean isUser() {
        return role.getId() == 1L;
    }

    public boolean isAuthorized() {
        return id != null;
    }
}
