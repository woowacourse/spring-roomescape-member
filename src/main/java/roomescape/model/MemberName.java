package roomescape.model;


import java.util.regex.Pattern;

public class MemberName {
    private final String name;
    private static final String NAME_FORMAT_REGEX = "^([가-힣]{2,5}|[a-zA-Z]{2,30})$";


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

    public static void validateNameFormat(String name) {
        if (Pattern.matches(NAME_FORMAT_REGEX, name)) {
            return;
        }
        throw new IllegalArgumentException("이름이 잘못되었습니다. " + name);
    }
}
