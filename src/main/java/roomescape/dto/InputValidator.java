package roomescape.dto;

public class InputValidator {

    public static void validateNotNull(Object... inputs) {
        for (Object input : inputs) {
            if (input == null) {
                throw new IllegalArgumentException("빈 값은 입력할 수 없다.");
            }
        }
    }
}
