package roomescape.common.validate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Validator {

    private final Class<?> clazz;

    public static Validator of(final Class<?> clazz) {
        return new Validator(clazz);
    }

    public Validator notNullField(final String fieldName,
                                  final Object target,
                                  final String fieldDescription) {
        if (target == null) {
            throw new InvalidInputException(
                    ValidationType.NULL_CHECK,
                    clazz.getSimpleName(),
                    fieldName,
                    fieldDescription
            );
        }
        return this;
    }

    public Validator notBlankField(final String fieldName,
                                   final String target,
                                   final String fieldDescription) {
        if (target == null || target.strip().isBlank()) {
            throw new InvalidInputException(
                    ValidationType.BLANK_CHECK,
                    clazz.getSimpleName(),
                    fieldName,
                    fieldDescription
            );
        }
        return this;
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    public enum ValidationType {
        NULL_CHECK("while checking null"),
        BLANK_CHECK("while checking blank");

        private final String description;
    }
}
