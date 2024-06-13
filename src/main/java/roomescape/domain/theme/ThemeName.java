package roomescape.domain.theme;

public class ThemeName {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;
    private final String name;

    public ThemeName(String name) {
        validateBlank(name);
        validateLength(name);
        this.name = name;
    }

    private void validateBlank(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("테마명이 존재하지 않습니다.");
        }
    }

    private void validateLength(String name) {
        if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("테마명 길이는 %d글자 이상, %d글자 이하여야 합니다.", MIN_LENGTH, MAX_LENGTH));
        }
    }

    public String getValue() {
        return name;
    }
}
