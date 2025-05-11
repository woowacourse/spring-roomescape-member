package roomescape.auth;

import roomescape.exception.InvalidRoleException;

public enum Role {

    ADMIN, MEMBER;

    public static Role of(String value) {
        try {
            return Role.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new InvalidRoleException();
        }
    }
}
