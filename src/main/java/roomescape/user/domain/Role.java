package roomescape.user.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum Role {
    NORMAL, ADMIN;

    public static List<Role> toList(String rawRoles) {
        if (rawRoles == null || rawRoles.trim().isEmpty()) {
            return List.of();
        }

        return Arrays.stream(rawRoles.split(","))
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .map(role -> {
                    try {
                        return Role.valueOf(role.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
