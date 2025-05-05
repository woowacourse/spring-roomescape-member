package roomescape.theme.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;

import java.net.URI;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldNameConstants(level = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class ThemeThumbnail {

    public static final String domainName = "예약 썸네일";

    private final URI value;

    public static ThemeThumbnail from(final String uri) {
        validate(uri);
        return new ThemeThumbnail(URI.create(uri));
    }

    private static void validate(final String value) {
        Validator.of(ThemeThumbnail.class)
                .validateUriFormat(Fields.value, value, domainName);
    }
}
