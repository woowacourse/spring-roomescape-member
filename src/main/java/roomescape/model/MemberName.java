package roomescape.model;

import static roomescape.util.FormatValidator.validateNameFormat;

public class MemberName {
    private final String name;

    public MemberName(String name) {
        validateNotNull(name);
        validateNameFormat(name);
        this.name = name;
    }

    private void validateNotNull(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("이름은 null이 될 수 없습니다.");
        }
    }

    public String getName() {
        return name;
    }
}
