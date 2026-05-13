package roomescape.domain;

import lombok.Getter;

@Getter
public class PersonName {

    private final String name;

    public PersonName(final String value) {
        validate(value);
        this.name = value;
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 비워둘 수 없습니다.");
        }
    }
}
