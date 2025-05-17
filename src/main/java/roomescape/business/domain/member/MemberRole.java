package roomescape.business.domain.member;

import java.util.stream.Stream;

public enum MemberRole {

    MEMBER("ROLE_MEMBER"),
    ADMIN("ROLE_ADMIN");

    private final String value;

    MemberRole(String value) {
        this.value = value;
    }

    public static MemberRole from(String value) {
        return Stream.of(values())
                .filter(role -> role.value.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 계정입니다. 관리자에게 문의하세요."));
    }

    public String value() {
        return value;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
