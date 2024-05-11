package roomescape.domain.member;

public enum Role {
    ADMIN, MEMBER;

    public static Role from(String name) {
        if (name.equals("ADMIN")) {
            return ADMIN;
        }
        if (name.equals("MEMBER")) {
            return MEMBER;
        }
        throw new IllegalArgumentException(String.format("입력한 값: %s, 역할이 존재하지 않습니다", name));
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
