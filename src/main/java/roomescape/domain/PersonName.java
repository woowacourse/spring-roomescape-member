package roomescape.domain;

import lombok.Getter;

@Getter
public class PersonName {

    private final String name;

    public PersonName(final String value) {
        this.name = value;
    }
}
