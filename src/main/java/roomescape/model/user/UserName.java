package roomescape.model.user;

import static roomescape.util.FormatValidator.validateNameFormat;

public class UserName {
    private final String name;

    public UserName(String name) {
        validateNameFormat(name);
        this.name = name;
    }

    public String getValue() {
        return name;
    }
}
