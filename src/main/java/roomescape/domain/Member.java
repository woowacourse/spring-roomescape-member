package roomescape.domain;

import roomescape.exception.ErrorType;
import roomescape.exception.InvalidClientRequestException;

public class Member {
    private final Long id;
    private final String email;
    private final String name;

    public Member(final Long id, final String email, final String name) {
        validId("id", id);
        validEmpty("email", email);
        validEmpty("name", name);
        this.id = id;
        this.email = email;
        this.name = name;
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
