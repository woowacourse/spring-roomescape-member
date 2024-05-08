package roomescape.domain;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private final String au;

    Role(String au) {
        this.au = au;
    }

    public static Role getR(String sau) {
        for (Role role : values()) {
            if (role.au.equals(sau)) {
                return role;
            }
        }
        throw new IllegalArgumentException("해당 role 없음");
    }
}
