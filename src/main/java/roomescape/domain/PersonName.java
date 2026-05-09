package roomescape.domain;

import lombok.Getter;

@Getter
public class PersonName {

    private final String name;

    private PersonName(final String value) {
        this.name = value;
    }

    public static PersonName from(String value) {
        return new PersonName(value);
    }

}
