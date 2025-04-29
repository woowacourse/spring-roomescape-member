package roomescape.model;

import static roomescape.util.FormatValidator.validateNameFormat;

public class UserName {
    private final String name;

    public UserName(String name) {
        validateNameFormat(name);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
