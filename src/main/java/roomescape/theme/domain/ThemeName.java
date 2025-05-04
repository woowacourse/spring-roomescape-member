package roomescape.theme.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldNameConstants(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class ThemeName {

    public static final String domainName = "테마 이름";

    private final String value;

    public static ThemeName from(final String name) {
        validate(name);
        return new ThemeName(name);
    }

    private static void validate(final String value) {
        Validator.of(ThemeName.class)
                .notBlankField(Fields.value, value, domainName);
    }
}
