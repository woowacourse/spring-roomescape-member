package roomescape.model;

import java.util.regex.Pattern;

public class MemberName {

    private static final int MAX_NAME_LENGTH = 30;
    private static final Pattern ONLY_NUMBER_PATTERN = Pattern.compile("^\\d+$");

    private final String value;

    public MemberName(final String value) {
        validateName(value);
        this.value = value;
    }

    private void validateName(final String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("멤버 이름이 비어 있습니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(String.format("(%s) 멤버 이름이 최대 길이인 %d자를 넘었습니다.", name, MAX_NAME_LENGTH));
        }
        if (ONLY_NUMBER_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(String.format("(%s) 숫자만으로 이루어진 멤버 이름은 사용할 수 없습니다.", name));
        }
    }

    public String getValue() {
        return value;
    }
}
