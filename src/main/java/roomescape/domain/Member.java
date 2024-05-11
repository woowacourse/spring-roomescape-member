package roomescape.domain;

import roomescape.domain.util.Validator;

public record Member(Long id, String name, String email, String password) {
    public static final int MAX_STRING_LENGTH = 255;

    public Member(String name, String email, String password) {
        this(null, name, email, password);
    }

    public Member {
        Validator.notEmpty(name);
        Validator.overSize(MAX_STRING_LENGTH, name);
    }

    public Member createWithId(Long id) {
        return new Member(id, name, email, password);
    }
}
