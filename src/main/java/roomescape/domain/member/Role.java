package roomescape.domain.member;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Role {

    USER("USER"),
    ADMIN("ADMIN");

    private static final Map<String, Role> CACHE = Arrays.stream(values())
            .collect(Collectors.toMap(Role::getRoleName, role -> role));

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public static Role of(String roleName) {
        Role role = CACHE.get(roleName);
        if (role == null) {
            throw new IllegalArgumentException("입력된 데이터로 Role을 조회할 수 없습니다.");
        }

        return role;
    }

    public String getRoleName() {
        return roleName;
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
