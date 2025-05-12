package roomescape.domain.enums;

public enum Role {
    ADMIN,
    USER;

    public static Role from(String value) {
        return Role.valueOf(value.toUpperCase()); // 예외 발생 가능
    }
}

