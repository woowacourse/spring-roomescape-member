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
public class ThemeThumbnail {

    public static final String domainName = "예약 썸네일";

    private final String value;

    public static ThemeThumbnail from(final String url) {
        validate(url);
        return new ThemeThumbnail(url);
    }

    private static void validate(final String value) {
        Validator.of(ThemeThumbnail.class)
                .notBlankField(Fields.value, value, domainName);
    }
}
