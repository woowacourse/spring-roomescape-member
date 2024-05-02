package roomescape.dto;

public class InputValidator {

    public static void validateNotNull(Object... inputs) {
        for (Object input : inputs) {
            if (input == null) {
                throw new IllegalArgumentException("null은 입력할 수 없다.");
            }
        }
    }

    public static void validateNotEmpty(String... inputs) {
        for (String input : inputs) {
            if (input.isEmpty()) {
                throw new IllegalArgumentException("빈 문자열은 입력할 수 없다.");
            }
        }
    }
}
