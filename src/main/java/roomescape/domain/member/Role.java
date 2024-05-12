package roomescape.domain.member;

public enum Role {
    ADMIN,
    MEMBER;

    public static Role from(String name) {
        try {
            return Role.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("존재하지 않는 역할입니다. (입력한 역할: " + name + ")");
        }
    }
}
