package roomescape.domain;

import roomescape.exception.CustomBadRequest;

public record Name(String value) {

    public Name {
        validate(value);
    }

    private void validate(final String name) {
        if (name.isBlank()) {
            throw new CustomBadRequest(String.format("name(%s)이 유효하지 않습니다.", name));
        }
    }
}
