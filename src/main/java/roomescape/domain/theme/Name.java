package roomescape.domain.theme;

public record Name(String name) {
    private static final int MAX_LENGTH = 20;

    public Name {
        validateName(name);
    }

    private void validateName(String name) {
        if (name.isEmpty() || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("테마이름은 %d자까지 만 가능합니다.", MAX_LENGTH));
        }
    }
}
