package roomescape.domain;

public enum Role {
    ADMIN, USER;

    public boolean isNotAdmin() {
        return this != ADMIN;
    }
}
