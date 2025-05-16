package roomescape.model.user;

import static roomescape.util.FormatValidator.validateNameFormat;

public class Name {
    private final String name;

    public Name(String name) {
        validateNameFormat(name);
        this.name = name;
    }

    public String getValue() {
        return name;
    }
}
