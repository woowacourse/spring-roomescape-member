package roomescape.business.domain;

public enum Role {
    UNKNOWN(0),
    USER(1),
    ADMIN(2);

    private final int permission;

    Role(final int permission) {
        this.permission = permission;
    }

    public boolean hasPermission(final Role role) {
        return this.permission >= role.permission;
    }
}
