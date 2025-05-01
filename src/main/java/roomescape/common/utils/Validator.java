package roomescape.common.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Validator {

    private final Class<?> clazz;

    public static Validator of(final Class<?> clazz) {
        return new Validator(clazz);
    }

    public Validator notNullField(final String fieldName, final Object target) {
        if (target == null) {
            throw new IllegalArgumentException(clazz.getSimpleName() + "." + fieldName + " 은(는) null일 수 없습니다.");
        }
        return this;
    }

    public Validator notBlankField(final String fieldName, final String target) {
        if (target == null || target.isBlank()) {
            throw new IllegalArgumentException(clazz.getSimpleName() + "." + fieldName + " 은(는) 비어있을 수 없습니다.");
        }
        return this;
    }
}
