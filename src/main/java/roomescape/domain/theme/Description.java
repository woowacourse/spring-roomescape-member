package roomescape.domain.theme;

public record Description(String description) {
    private static final int MAX_LENGTH = 10;

    public Description {
        validateDescription(description);
    }

    private void validateDescription(String description) {
        if (description.length() < MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("설명은 %d글자 이상 입력해주세요.", MAX_LENGTH));
        }
    }
}
