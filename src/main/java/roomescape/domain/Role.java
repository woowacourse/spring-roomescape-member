package roomescape.domain;

public enum Role {

    ADMIN("ADMIN"),
    USER("USER")
    ;

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
