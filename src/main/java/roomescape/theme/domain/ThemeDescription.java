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
public class ThemeDescription {

    public static final String domainName = "테마 설명";

    private final String value;

    public static ThemeDescription from(final String description) {
        validate(description);
        return new ThemeDescription(description);
    }

    private static void validate(final String value) {
        Validator.of(ThemeDescription.class)
                .validateNotBlank(Fields.value, value, domainName);
    }
}

