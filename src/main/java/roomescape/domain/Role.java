package roomescape.domain;

public enum Role {

    ADMIN,
    USER;

    public static Role findByName(String target) {
        if ("ADMIN".equals(target)) {
            return Role.ADMIN;
        }
        if ("USER".equals(target)) {
            return Role.USER;
        }
        throw new IllegalArgumentException("Invalid role: " + target); // TODO 예외 다르게
    }
}
