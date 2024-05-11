package roomescape.domain;

import roomescape.exception.ErrorType;
import roomescape.exception.InvalidClientRequestException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Member {
    private static final String ADMIN_ROLE = "ADMIN";
    private static Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$'");
    private final Long id;
    private final String email;
    private final String name;
    private final String role;

    public Member(final Long id, final String email, final String name, String role) {
        validId("id", id);
        validEmail(email);
        validEmpty("name", name);
        validEmpty("role", role);
        this.id = id;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    private void validEmail(String email) {
        validEmpty("email", email);
        Matcher matcher = EMAIL_REGEX.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidClientRequestException(ErrorType.INVALID_DATA_TYPE, "email", email);
        }
    }

    private void validId(final String fieldName, final Long value) {
        if (value == null || value <= 0) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, fieldName, "");
        }
    }

    private void validEmpty(final String fieldName, final String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new InvalidClientRequestException(ErrorType.EMPTY_VALUE_NOT_ALLOWED, fieldName, "");
        }
    }

    public boolean isAdmin() {
        return ADMIN_ROLE.equals(role);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
