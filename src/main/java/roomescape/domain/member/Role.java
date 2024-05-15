package roomescape.domain.member;

import java.util.stream.Stream;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }

    public static Role mapTo(String target) {
        return Stream.of(values()).
                filter(role -> role.getRole().equals(target))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 일치하는 역할이 없습니다."));
    }
}
